package com.sechkarev.hiraganateacherkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.sechkarev.hiraganateacherkmp.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() =
    ComposeUIViewController(
        configure = {
            startKoin {
                modules(
                    appModule,
                )
            }
        },
    ) { App() }
