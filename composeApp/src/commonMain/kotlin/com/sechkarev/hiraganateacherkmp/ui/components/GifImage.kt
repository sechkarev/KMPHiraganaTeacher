package com.sechkarev.hiraganateacherkmp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun GifImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
)
