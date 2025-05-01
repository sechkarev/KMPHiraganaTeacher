package com.sechkarev.hiraganateacherkmp.ui.game.challenges

import androidx.compose.ui.geometry.Offset
import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.Stroke
import com.sechkarev.hiraganateacherkmp.ui.game.ChallengeCompletionError
import com.sechkarev.hiraganateacherkmp.ui.game.TimerState

sealed interface ChallengeUiState {
    val challenge: Challenge
    val onWrittenTextClick: (String) -> Unit

    data class Completed(
        override val challenge: Challenge,
        override val onWrittenTextClick: (String) -> Unit = {},
        val solution: List<Stroke> = emptyList(),
    ) : ChallengeUiState

    data class Current(
        override val challenge: Challenge,
        override val onWrittenTextClick: (String) -> Unit = {},
        val drawnStrokes: List<Stroke> = emptyList(),
        val currentStroke: Stroke? = null,
        val completionError: ChallengeCompletionError? = null,
        val timerState: TimerState = TimerState.Idle,
        val onDragStart: () -> Unit = {},
        val onDragEnd: () -> Unit = {},
        val onDrag: (Offset) -> Unit = {},
        val onDragCancel: () -> Unit = {},
    ) : ChallengeUiState
}
