package com.sechkarev.hiraganateacherkmp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.animated_character_drawing_description
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnimatedHiraganaCharacter(
    animationRes: DrawableResource,
    animatedCharacter: Char,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier =
            Modifier
                .drawingCanvasSize()
                .border(width = 1.dp, color = Color.Black)
                .then(modifier),
        painter = painterResource(animationRes),
        contentScale = ContentScale.FillWidth,
        contentDescription = stringResource(Res.string.animated_character_drawing_description, animatedCharacter.toString()),
    )
}
