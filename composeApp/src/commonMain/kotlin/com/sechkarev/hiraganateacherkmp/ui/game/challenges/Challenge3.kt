package com.sechkarev.hiraganateacherkmp.ui.game.challenges

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.HiraganaCharacter
import com.sechkarev.hiraganateacherkmp.ui.components.AnimatedHiraganaCharacter
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.challenge3_task
import kmphiraganateacher.composeapp.generated.resources.challenge3_text
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_e
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Challenge3(
    challengeState: ChallengeUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp).then(modifier),
    ) {
        Text(
            text = stringResource(Res.string.challenge3_text),
        )
        Spacer(Modifier.height(16.dp))
        AnimatedHiraganaCharacter(
            resourceName = "hiragana_animated_e",
            animatedCharacter = HiraganaCharacter.E.spelling,
            onClick = { challengeState.onWrittenTextClick(HiraganaCharacter.E.spelling.toString()) },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.challenge3_task),
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
private fun PreviewChallenge3() {
    MaterialTheme {
        Challenge3(ChallengeUiState.Completed(Challenge.Challenge3))
    }
}
