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
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge18
import com.sechkarev.hiraganateacherkmp.ui.game.challenges.Challenge19
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
//     todo: move ids to a separate class
    // todo: add remaining challenges
    // todo: rename composables
    return when (challenge.name) {
        "i_beginning" -> Challenge1(challengeUiState)
        "i_repetition" -> Challenge2(challengeUiState)
        "e_introduction" -> Challenge3(challengeUiState)
        "e_repetition" -> Challenge4(challengeUiState)
        "e_no_hint" -> Challenge5(challengeUiState)
        "back_to_i" -> Challenge6(challengeUiState)
        "new_word_ie" -> Challenge7(challengeUiState)
        "new_word_iie" -> Challenge8(challengeUiState)
        "are_you_afraid" -> Challenge9(challengeUiState)
        "iie_timed_10_sec" -> Challenge10(challengeUiState)
        "iie_timed_5_sec" -> Challenge11(challengeUiState)
        "ha_introduction" -> Challenge12(challengeUiState)
        "ha_repetition" -> Challenge13(challengeUiState)
        "new_word_hai" -> Challenge14(challengeUiState)
        "hai_timed_5_sec" -> Challenge15(challengeUiState)
        "a_introduction" -> Challenge16(challengeUiState)
        "a_repetition" -> Challenge17(challengeUiState)
        "new_word_ai" -> Challenge18(challengeUiState)
        "ai_decorated_canvas" -> Challenge19(challengeUiState)
        else -> {} // todo: throw an exception at some point
    }
}
