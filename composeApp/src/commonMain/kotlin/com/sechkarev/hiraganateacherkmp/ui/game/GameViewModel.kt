package com.sechkarev.hiraganateacherkmp.ui.game

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.model.GameProgress
import com.sechkarev.hiraganateacherkmp.model.Point
import com.sechkarev.hiraganateacherkmp.model.Stroke
import com.sechkarev.hiraganateacherkmp.textrecognition.TextRecognizer2
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface DrawingAction {
    data object OnNewPathStart : DrawingAction

    data class OnDraw(
        val offset: Offset,
    ) : DrawingAction

    data object OnPathEnd : DrawingAction

    data object OnClearCanvasClick : DrawingAction
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
) : ViewModel() {
    private val _gameUiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState())
    val gameUiState = _gameUiState.asStateFlow()

    private var timerJob: Job? = null

    // todo: I probably want to get rid of this flow and react to wins manually
    init {
        viewModelScope.launch {
            gameRepository
                .gameProgress
                .collect { gameProgress ->
                    _gameUiState.update {
                        GameUiState(
                            gameProgress = gameProgress,
                            timerState = TimerState.Idle,
                            drawnStrokes = emptyList(),
                            currentStroke = null,
                            challengeCompletionError = null,
                        )
                    }
                    textRecognizer.cleanCurrentData()
                }
        }
    }

    fun onAction(action: DrawingAction) {
        when (action) {
            DrawingAction.OnClearCanvasClick -> onClearCanvasClick()
            is DrawingAction.OnDraw -> onDraw(action.offset)
            is DrawingAction.OnNewPathStart -> onNewPathStart()
            is DrawingAction.OnPathEnd -> onPathEnd()
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
        if (textRecognizer.currentStrokeAmount() >= currentChallenge.answer.requiredStrokes) {
            viewModelScope.launch {
                cancelTimer() // todo: visible lag
                val recognizedText = textRecognizer.recognizeCurrentText()
                if (recognizedText == currentChallenge.answer.answerText) {
                    gameRepository.insertSolution(
                        challengeId = currentChallenge.name,
                        solution = _gameUiState.value.drawnStrokes,
                    )
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
        if (textRecognizer.currentStrokeAmount() >= currentChallenge.answer.requiredStrokes) {
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

    private fun onClearCanvasClick() {
        cleanCanvas()
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
}
