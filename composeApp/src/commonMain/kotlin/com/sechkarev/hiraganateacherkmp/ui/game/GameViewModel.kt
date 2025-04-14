package com.sechkarev.hiraganateacherkmp.ui.game

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.model.GameProgress
import com.sechkarev.hiraganateacherkmp.model.Point
import com.sechkarev.hiraganateacherkmp.model.Stroke
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
    // todo: add other kinds of errors when I create timed challenges
}

class GameViewModel(
    private val gameRepository: GameRepository,
    // private val textRecognizer: TextRecognizer,
) : ViewModel() {
    data class GameUiState(
        val gameProgress: GameProgress,
        val drawnStrokes: List<Stroke>,
        val currentStroke: Stroke?,
        val challengeCompletionError: ChallengeCompletionError?,
    )

    private val _gameUiState: MutableStateFlow<GameUiState> =
        MutableStateFlow(
            GameUiState(
                gameProgress =
                    GameProgress(
                        solvedChallenges = emptyList(),
                        currentChallenge = null,
                        gameCompleted = false,
                    ),
                drawnStrokes = emptyList(),
                currentStroke = null,
                challengeCompletionError = null,
            ),
        )
    val gameUiState = _gameUiState.asStateFlow()

    // private var inkBuilder = Ink.builder()
    // private var strokeBuilder = Ink.Stroke.builder()

    init {
        viewModelScope.launch {
            gameRepository
                .gameProgress
                .collect { gameState ->
                    _gameUiState.update {
                        GameUiState(
                            gameProgress = gameState,
                            drawnStrokes = emptyList(),
                            currentStroke = null,
                            challengeCompletionError = null,
                        )
                    }
                    // inkBuilder = Ink.builder()
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
//        inkBuilder.addStroke(strokeBuilder.build())
//        val ink = inkBuilder.build()
//        if (ink.strokes.size >= currentChallenge.answer.requiredStrokes) {
//            viewModelScope.launch {
//                val recognizedText = textRecognizer.recognizeText(ink)
//                if (recognizedText == currentChallenge.answer.answerText) {
//                    gameRepository.insertSolution(
//                        challengeId = currentChallenge.name,
//                        solution = _gameUiState.value.drawnStrokes,
//                    )
//                } else {
//                    _gameUiState.update {
//                        it.copy(challengeCompletionError = ChallengeCompletionError.WrongText(recognizedText))
//                    }
//                }
//            }
//        }
    }

    private fun onNewPathStart() {
        val currentChallenge = _gameUiState.value.gameProgress.currentChallenge ?: return
//        if (_gameUiState.value.challengeCompletionError != null) {
//            cleanCanvas()
//        } else if (inkBuilder.build().strokes.size >= currentChallenge.answer.requiredStrokes) {
//            return
//        }
        _gameUiState.update { uiState ->
            uiState.copy(
                currentStroke = Stroke(path = emptyList()),
            )
        }
        // strokeBuilder = Ink.Stroke.builder()
    }

    private fun onDraw(offset: Offset) {
        _gameUiState.update { uiState ->
            uiState.copy(
                currentStroke =
                    uiState.currentStroke?.copy(
                        path = uiState.currentStroke.path + Point(offset.x, offset.y),
                    ),
            )
        }
        // strokeBuilder.addPoint(Ink.Point.create(offset.x, offset.y))
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
        // inkBuilder = Ink.builder()
    }
}
