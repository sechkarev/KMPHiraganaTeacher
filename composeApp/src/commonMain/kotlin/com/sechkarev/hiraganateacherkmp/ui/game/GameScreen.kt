package com.sechkarev.hiraganateacherkmp.ui.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.Stroke
import com.sechkarev.hiraganateacherkmp.ui.components.TopBarWithBackIcon
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge1
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge10
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge11
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge12
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge13
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge14
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge15
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge16
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge17
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge2
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge3
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge4
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge5
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge6
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge7
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge8
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge9
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.ChallengeUiState
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.GameCompletedMessage
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
//     todo: can I make this mapping better?
    return when (challenge) {
        Challenge.Challenge1 -> Challenge1(challengeUiState)
        Challenge.Challenge2 -> Challenge2(challengeUiState)
        Challenge.Challenge3 -> Challenge3(challengeUiState)
        Challenge.Challenge4 -> Challenge4(challengeUiState)
        Challenge.Challenge5 -> Challenge5(challengeUiState)
        Challenge.Challenge6 -> Challenge6(challengeUiState)
        Challenge.Challenge7 -> Challenge7(challengeUiState)
        Challenge.Challenge8 -> Challenge8(challengeUiState)
        Challenge.Challenge9 -> Challenge9(challengeUiState)
        Challenge.Challenge10 -> Challenge10(challengeUiState)
        Challenge.Challenge11 -> Challenge11(challengeUiState)
        Challenge.Challenge12 -> Challenge12(challengeUiState)
        Challenge.Challenge13 -> Challenge13(challengeUiState)
        Challenge.Challenge14 -> Challenge14(challengeUiState)
        Challenge.Challenge15 -> Challenge15(challengeUiState)
        Challenge.Challenge16 -> Challenge16(challengeUiState)
        Challenge.Challenge17 -> Challenge17(challengeUiState)
    }
}
