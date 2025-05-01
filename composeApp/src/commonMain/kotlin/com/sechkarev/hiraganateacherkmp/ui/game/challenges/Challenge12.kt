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
import kmphiraganateacher.composeapp.generated.resources.challenge12_task
import kmphiraganateacher.composeapp.generated.resources.challenge12_text
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_ha
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Challenge12(
    challengeState: ChallengeUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp).then(modifier),
    ) {
        Text(
            text = stringResource(Res.string.challenge12_text),
        )
        Spacer(Modifier.height(16.dp))
        AnimatedHiraganaCharacter(
            resourceName = "hiragana_animated_ha",
            animatedCharacter = HiraganaCharacter.HA.spelling,
            onClick = { challengeState.onWrittenTextClick(HiraganaCharacter.HA.spelling.toString()) },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.challenge12_task),
        )
        Spacer(Modifier.height(16.dp))
        DrawingChallenge(
            challengeState = challengeState,
            hintImageRes = Res.drawable.hiragana_static_ha,
            drawingLineThickness = 48f,
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Preview
@Composable
private fun PreviewChallenge12() {
    MaterialTheme {
        Challenge12(ChallengeUiState.Completed(Challenge.Challenge12))
    }
}
