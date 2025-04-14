package com.sechkarev.hiraganateacherkmp.ui.game.challenges

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.game_completion_button_text
import kmphiraganateacher.composeapp.generated.resources.game_completion_message
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameCompletedMessage(
    onReturnToMainMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.game_completion_message),
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onReturnToMainMenuClick,
            content = {
                Text(stringResource(Res.string.game_completion_button_text))
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}
