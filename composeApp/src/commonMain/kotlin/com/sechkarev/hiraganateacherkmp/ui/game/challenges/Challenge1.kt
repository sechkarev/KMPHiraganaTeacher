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
import com.sechkarev.hiraganateacherkmp.ui.components.AnimatedHiraganaCharacter
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.challenge1_task
import kmphiraganateacher.composeapp.generated.resources.challenge1_text
import kmphiraganateacher.composeapp.generated.resources.challenge1_welcome_message
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_i
import org.jetbrains.compose.resources.stringResource

@Composable
fun Challenge1(
    challengeState: ChallengeUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp).then(modifier),
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(Res.string.challenge1_welcome_message),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(Res.string.challenge1_text),
        )
        Spacer(Modifier.height(16.dp))
        AnimatedHiraganaCharacter(
            resourceName = "hiragana_animated_i",
            animatedCharacter = challengeState.challenge.newCharacter?.spelling,
            onClick = {
                challengeState.onWrittenTextClick(
                    challengeState.challenge.newCharacter
                        ?.spelling
                        .toString(),
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.challenge1_task),
        )
        Spacer(Modifier.height(16.dp))
        DrawingChallenge(
            challengeState = challengeState,
            hintImageRes = Res.drawable.hiragana_static_i,
            drawingLineThickness = 48f,
        )
        Spacer(Modifier.height(16.dp))
    }
}
