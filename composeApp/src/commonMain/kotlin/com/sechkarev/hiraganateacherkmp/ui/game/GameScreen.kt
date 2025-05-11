package com.sechkarev.hiraganateacherkmp.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.Stroke
import com.sechkarev.hiraganateacherkmp.model.UiComponent
import com.sechkarev.hiraganateacherkmp.ui.components.AnimatedHiraganaCharacter
import com.sechkarev.hiraganateacherkmp.ui.components.TopBarWithBackIcon
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.ChallengeUiState
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.DrawingChallenge
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.GameCompletedMessage
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.NewWord
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameScreen(
    onBackClick: () -> Unit,
    onReturnToMainMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = koinViewModel(),
) {
    val gameUiState by viewModel.gameUiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBarWithBackIcon(
                title = stringResource(Res.string.app_name),
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            // always scroll to the very bottom, to the current challenge
            state = rememberLazyListState(initialFirstVisibleItemIndex = Int.MAX_VALUE),
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        ) {
            items(gameUiState.gameProgress.solvedChallenges) {
                Challenge(
                    gameUiState = gameUiState,
                    challenge = it.challenge,
                    solution = it.solution,
                    onAction = viewModel::onAction,
                )
            }
            val currentChallenge = gameUiState.gameProgress.currentChallenge
            if (currentChallenge != null) {
                item {
                    Challenge(
                        gameUiState = gameUiState,
                        challenge = currentChallenge,
                        solution = null,
                        onAction = viewModel::onAction,
                    )
                }
            }
            if (gameUiState.gameProgress.gameCompleted) {
                item {
                    GameCompletedMessage(
                        onReturnToMainMenuClick = onReturnToMainMenuClick,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun Challenge(
    gameUiState: GameUiState,
    challenge: Challenge,
    solution: List<Stroke>?,
    onAction: (DrawingAction) -> Unit,
) {
    // todo: remove the ui state?
    val challengeUiState =
        if (solution == null) {
            ChallengeUiState.Current(
                challenge = challenge,
                drawnStrokes = gameUiState.drawnStrokes,
                currentStroke = gameUiState.currentStroke,
                completionError = gameUiState.challengeCompletionError,
                timerState = gameUiState.timerState,
                onWrittenTextClick = { onAction(DrawingAction.OnTextClick(it)) },
                onDragStart = { onAction(DrawingAction.OnNewPathStart) },
                onDragEnd = { onAction(DrawingAction.OnPathEnd) },
                onDrag = { onAction(DrawingAction.OnDraw(it)) },
                onDragCancel = { onAction(DrawingAction.OnPathEnd) },
            )
        } else {
            ChallengeUiState.Completed(
                challenge = challenge,
                solution = solution,
                onWrittenTextClick = { onAction(DrawingAction.OnTextClick(it)) },
            )
        }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
    ) {
        challenge.uiComponents.map {
            when (it) {
                is UiComponent.Animation -> {
                    AnimatedHiraganaCharacter(
                        resourceName = it.animationId,
                        animatedCharacter = challenge.newCharacter?.spelling,
                        onClick = {
                            onAction(DrawingAction.OnTextClick(challenge.newCharacter?.spelling.toString()))
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
                is UiComponent.DrawingChallenge -> {
                    DrawingChallenge(
                        challengeState = challengeUiState,
                        hintImageRes = it.hintResource,
                        canvasDecoration = it.decoration,
                        drawingLineThickness =
                            if (challenge.challengeAnswer.answerText.length == 1) {
                                48f
                            } else {
                                // todo: more branches?
                                32f
                            },
                    )
                }
                is UiComponent.Headline -> {
                    Text(
                        text = stringResource(it.textResource),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
                is UiComponent.NewWord -> {
                    NewWord(
                        dictionaryItem = it.word,
                    )
                }
                is UiComponent.Text -> {
                    Text(
                        text = stringResource(it.textResource),
                    )
                }
            }
        }
    }
}
