package com.sechkarev.hiraganateacherkmp.ui.game

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.model.GameProgress
import com.sechkarev.hiraganateacherkmp.model.Point
import com.sechkarev.hiraganateacherkmp.model.SolvedChallenge
import com.sechkarev.hiraganateacherkmp.model.Stroke
import com.sechkarev.hiraganateacherkmp.textrecognition.TextRecognizer2
import com.sechkarev.hiraganateacherkmp.tts.TextToSpeechEngine
import com.sechkarev.hiraganateacherkmp.ui.utils.stateInWhileSubscribed
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DrawingAction {
    data object OnNewPathStart : DrawingAction

    data class OnDraw(
        val offset: Offset,
    ) : DrawingAction

    data object OnPathEnd : DrawingAction

    data class OnTextClick(
        val text: String,
    ) : DrawingAction
}

sealed interface ChallengeCompletionError {
    data class WrongText(
        val text: String,
    ) : ChallengeCompletionError

    data object Timeout : ChallengeCompletionError
}

sealed interface TimerState {
    data object Idle : TimerState

    data class Running(
        val remainingSeconds: Int,
    ) : TimerState
}

data class GameUiState(
    val gameProgress: GameProgress = GameProgress(),
    val drawnStrokes: List<Stroke> = emptyList(),
    val currentStroke: Stroke? = null,
    val challengeCompletionError: ChallengeCompletionError? = null,
    val timerState: TimerState = TimerState.Idle,
)

class GameViewModel(
    private val gameRepository: GameRepository,
    private val textRecognizer: TextRecognizer2,
    private val textToSpeechEngine: TextToSpeechEngine,
) : ViewModel() {
    private val _gameUiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState())
    val gameUiState =
        _gameUiState
            .onStart {
                val gameProgress = gameRepository.retrieveGameProgress()
                _gameUiState.update {
                    GameUiState(
                        gameProgress = gameProgress,
                        timerState = TimerState.Idle,
                        drawnStrokes = emptyList(),
                        currentStroke = null,
                        challengeCompletionError = null,
                    )
                }
            }.stateInWhileSubscribed(viewModelScope, GameUiState())

    private var timerJob: Job? = null

    fun onAction(action: DrawingAction) {
        when (action) {
            is DrawingAction.OnDraw -> onDraw(action.offset)
            is DrawingAction.OnNewPathStart -> onNewPathStart()
            is DrawingAction.OnPathEnd -> onPathEnd()
            is DrawingAction.OnTextClick -> pronounceText(action.text)
        }
    }

    private fun onPathEnd() {
        val currentPath = _gameUiState.value.currentStroke ?: return
        val currentChallenge = _gameUiState.value.gameProgress.currentChallenge ?: return
        _gameUiState.update { uiState ->
            uiState.copy(
                currentStroke = null,
                drawnStrokes = uiState.drawnStrokes.plus(currentPath),
            )
        }
        textRecognizer.completeStroke()
        if (textRecognizer.currentStrokeAmount() >= currentChallenge.challengeAnswer.requiredStrokes) {
            viewModelScope.launch {
                cancelTimer() // todo: visible lag
                val recognizedText = textRecognizer.recognizeCurrentText()
                if (recognizedText == currentChallenge.challengeAnswer.answerText) {
                    textRecognizer.cleanCurrentData()
                    gameRepository.insertSolution(
                        challengeId = currentChallenge.name,
                        solution = _gameUiState.value.drawnStrokes,
                    )
                    val nextChallenge = gameRepository.retrieveNextChallenge(currentChallenge.name)
                    _gameUiState.update {
                        GameUiState(
                            gameProgress =
                                GameProgress(
                                    solvedChallenges =
                                        it
                                            .gameProgress
                                            .solvedChallenges
                                            .plus(SolvedChallenge(currentChallenge, it.drawnStrokes)),
                                    currentChallenge = nextChallenge,
                                    gameCompleted = nextChallenge == null,
                                ),
                            timerState = TimerState.Idle,
                            drawnStrokes = emptyList(),
                            currentStroke = null,
                            challengeCompletionError = null,
                        )
                    }
                } else {
                    _gameUiState.update {
                        it.copy(challengeCompletionError = ChallengeCompletionError.WrongText(recognizedText))
                    }
                }
            }
        }
    }

    private fun onNewPathStart() {
        val currentChallenge = _gameUiState.value.gameProgress.currentChallenge ?: return
        if (_gameUiState.value.challengeCompletionError != null) {
            cleanCanvas()
        }
        if (textRecognizer.currentStrokeAmount() >= currentChallenge.challengeAnswer.requiredStrokes) {
            return
        }
        if (_gameUiState.value.timerState == TimerState.Idle && currentChallenge.secondsToComplete != null) {
            startTimer(currentChallenge.secondsToComplete)
        }
        _gameUiState.update { uiState ->
            uiState.copy(
                currentStroke = Stroke(path = emptyList()),
            )
        }
        textRecognizer.startNewStroke()
    }

    private fun onDraw(offset: Offset) {
        val newPoint = Point(offset.x, offset.y)
        _gameUiState.update { uiState ->
            uiState.copy(
                currentStroke =
                    uiState.currentStroke?.copy(
                        path = uiState.currentStroke.path + newPoint,
                    ),
            )
        }
        textRecognizer.addNewPoint(newPoint)
    }

    private fun startTimer(seconds: Int) {
        timerJob =
            viewModelScope.launch {
                _gameUiState.update {
                    it.copy(
                        timerState = TimerState.Running(seconds),
                        challengeCompletionError = null,
                    )
                }
                repeat(seconds - 1) { currentCall ->
                    delay(1000)
                    _gameUiState.update {
                        it.copy(
                            timerState = TimerState.Running(seconds - currentCall - 1),
                        )
                    }
                }
                delay(1000)
                _gameUiState.update {
                    it.copy(
                        timerState = TimerState.Idle,
                        challengeCompletionError = ChallengeCompletionError.Timeout,
                    )
                }
            }
    }

    private fun cancelTimer() {
        timerJob?.cancel()
        _gameUiState.update {
            it.copy(
                timerState = TimerState.Idle,
                challengeCompletionError = null,
            )
        }
    }

    private fun cleanCanvas() {
        _gameUiState.update { uiState ->
            uiState.copy(
                currentStroke = null,
                drawnStrokes = emptyList(),
                challengeCompletionError = null,
            )
        }
        textRecognizer.cleanCurrentData()
    }

    private fun pronounceText(text: String) {
        textToSpeechEngine.pronounce(text)
    }
}
