package com.sechkarev.hiraganateacherkmp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.gif.GifDecoder
import coil3.request.ImageRequest

@Composable
actual fun GifImage(
    url: String,
    contentDescription: String,
    modifier: Modifier,
) {
    val context = LocalContext.current
    val imageLoader =
        ImageLoader
            .Builder(context)
            .components {
                add(GifDecoder.Factory())
            }.build()

    AsyncImage(
        model =
            ImageRequest
                .Builder(context)
                .data(url)
                .build(),
        contentDescription = contentDescription,
        imageLoader = imageLoader,
        modifier = modifier,
        contentScale = ContentScale.FillBounds,
    )
}
