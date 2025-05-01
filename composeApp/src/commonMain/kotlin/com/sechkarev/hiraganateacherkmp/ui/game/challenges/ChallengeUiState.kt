package com.sechkarev.hiraganateacherkmp.ui.game.challenges

import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.Stroke
import com.sechkarev.hiraganateacherkmp.ui.game.ChallengeCompletionError
import com.sechkarev.hiraganateacherkmp.ui.game.DrawingAction
import com.sechkarev.hiraganateacherkmp.ui.game.TimerState

sealed interface ChallengeUiState {
    val challenge: Challenge

    data class Completed(
        override val challenge: Challenge,
        val solution: List<Stroke>,
    ) : ChallengeUiState

    data class Current(
        override val challenge: Challenge,
        val drawnStrokes: List<Stroke>,
        val currentStroke: Stroke?,
        val completionError: ChallengeCompletionError?,
        val timerState: TimerState,
        val onAction: (DrawingAction) -> Unit,
    ) : ChallengeUiState
}
