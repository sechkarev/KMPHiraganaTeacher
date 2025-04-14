package com.sechkarev.hiraganateacherkmp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sechkarev.hiraganateacherkmp.navigation.NavGraph

@Composable
fun App(modifier: Modifier = Modifier) {
    MaterialTheme {
        NavGraph(modifier)
    }
}
