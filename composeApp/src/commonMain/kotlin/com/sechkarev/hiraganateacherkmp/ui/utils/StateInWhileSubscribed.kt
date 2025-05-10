package com.sechkarev.hiraganateacherkmp.ui.utils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

// see https://proandroiddev.com/loading-initial-data-in-launchedeffect-vs-viewmodel-f1747c20ce62
fun <T> Flow<T>.stateInWhileSubscribed(scope: CoroutineScope, initialValue: T): StateFlow<T> {
    return stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = initialValue,
    )
}