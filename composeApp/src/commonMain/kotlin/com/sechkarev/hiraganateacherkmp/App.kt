package com.sechkarev.hiraganateacherkmp

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sechkarev.hiraganateacherkmp.navigation.NavGraph

@Composable
fun App(modifier: Modifier = Modifier) {
    MaterialTheme {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            modifier = modifier.fillMaxSize(),
        ) {
            NavGraph(modifier = Modifier.padding(it))
        }
    }
}
