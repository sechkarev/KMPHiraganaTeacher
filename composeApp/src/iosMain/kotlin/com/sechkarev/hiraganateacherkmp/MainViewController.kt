package com.sechkarev.hiraganateacherkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.sechkarev.hiraganateacherkmp.di.initKoin

fun MainViewController() =
    ComposeUIViewController(
        configure = {
            initKoin()
        },
    ) { App() }
