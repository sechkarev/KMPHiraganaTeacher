package com.sechkarev.hiraganateacherkmp.ui.game.challenges

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.sechkarev.hiraganateacherkmp.ui.components.drawingCanvasSize
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun ImageWithSubtitle(
    imageResource: DrawableResource,
    imageDescription: String,
    subtitle: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Image(
            painter = painterResource(resource = imageResource),
            contentDescription = imageDescription,
            modifier =
                Modifier
                    .drawingCanvasSize()
                    .align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            modifier =
                Modifier
                    .align(Alignment.CenterHorizontally),
        )
    }
}
