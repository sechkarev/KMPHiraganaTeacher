package com.sechkarev.hiraganateacherkmp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

expect suspend fun getResourceFile(fileResourcePath: String): File

@Composable
fun GifImage(
    fileResourcePath: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    var file: File? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        file = getResourceFile(fileResourcePath)
    }

    file?.let {
        KamelImage(
            resource = { asyncPainterResource(it) },
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}
