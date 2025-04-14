package com.sechkarev.hiraganateacherkmp.ui.game.challenges

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sechkarev.hiraganateacherkmp.model.Challenge
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.challenge4_task
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_e
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Challenge4(
    challengeState: ChallengeUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp).then(modifier),
    ) {
        Text(
            text = stringResource(Res.string.challenge4_task),
        )
        Spacer(Modifier.height(16.dp))
        DrawingChallenge(
            challengeState = challengeState,
            hintImageRes = Res.drawable.hiragana_static_e,
            drawingLineThickness = 48f,
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun PreviewChallenge4() {
    MaterialTheme {
        Challenge4(ChallengeUiState.Completed(Challenge.Challenge4, emptyList()))
    }
}
