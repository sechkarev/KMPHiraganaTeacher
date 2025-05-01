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
import com.sechkarev.hiraganateacherkmp.model.DictionaryItem
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.challenge18_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge18_task
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Challenge18(
    challengeState: ChallengeUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp).then(modifier),
    ) {
        Text(stringResource(Res.string.challenge18_congratulations))
        Spacer(Modifier.height(16.dp))
        NewWord(DictionaryItem.AI)
        Spacer(Modifier.height(16.dp))
        Text(stringResource(Res.string.challenge18_task))
        Spacer(Modifier.height(16.dp))
        DrawingChallenge(
            challengeState = challengeState,
            hintImageRes = null,
            drawingLineThickness = 32f,
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun PreviewChallenge18() {
    MaterialTheme {
        Challenge9(ChallengeUiState.Completed(Challenge.Challenge9))
    }
}
