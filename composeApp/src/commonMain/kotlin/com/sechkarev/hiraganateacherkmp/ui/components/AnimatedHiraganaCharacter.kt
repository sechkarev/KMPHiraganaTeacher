package com.sechkarev.hiraganateacherkmp.ui.components

import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.animated_character_drawing_description
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AnimatedHiraganaCharacter(
    resourceName: String, // todo: looks really unsafe
    animatedCharacter: Char,
    modifier: Modifier = Modifier,
) {
    GifImage(
        modifier =
            Modifier
                .drawingCanvasSize()
                .border(width = 1.dp, color = Color.Black)
                .then(modifier),
        contentDescription = stringResource(Res.string.animated_character_drawing_description, animatedCharacter.toString()),
        fileResourcePath = "drawable/$resourceName.gif",
    )
}
