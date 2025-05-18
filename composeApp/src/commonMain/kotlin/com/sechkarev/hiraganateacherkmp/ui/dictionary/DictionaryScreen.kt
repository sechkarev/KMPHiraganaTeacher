package com.sechkarev.hiraganateacherkmp.ui.dictionary

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sechkarev.hiraganateacherkmp.ui.components.TopBarWithBackIcon
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.dictionary_screen_subtitle
import kmphiraganateacher.composeapp.generated.resources.dictionary_screen_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DictionaryScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DictionaryViewModel = koinViewModel(),
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBarWithBackIcon(
                title = stringResource(Res.string.dictionary_screen_title),
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->

        // todo: add a link to each item to lead to actual dictionary (e.g. https://jisho.org/word/%E6%84%9B). These links are localisation-aware.
        val uiState = viewModel.uiState.collectAsStateWithLifecycle()

        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            val dictionaryItems = uiState.value.items

            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = stringResource(Res.string.dictionary_screen_title),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text =
                        stringResource(
                            Res.string.dictionary_screen_subtitle,
                            dictionaryItems.size,
                        ),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(24.dp))
            }
            items(dictionaryItems) { dictionaryItem ->
                Row {
                    Spacer(Modifier.width(16.dp))
                    Text(text = dictionaryItem.original, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.weight(1f))
                    Text(text = stringResource(dictionaryItem.translation), style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.width(16.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
