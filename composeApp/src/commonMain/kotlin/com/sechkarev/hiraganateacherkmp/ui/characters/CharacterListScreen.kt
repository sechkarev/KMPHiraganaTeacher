package com.sechkarev.hiraganateacherkmp.ui.characters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sechkarev.hiraganateacherkmp.model.HiraganaCharacter
import com.sechkarev.hiraganateacherkmp.model.HiraganaCharacterOnGrid
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.characters_screen_subtitle
import kmphiraganateacher.composeapp.generated.resources.characters_screen_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CharacterListScreen(
    modifier: Modifier = Modifier,
    viewModel: CharacterListViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle(CharacterListViewModel.UiState(emptyList()))

    Column(modifier = modifier.fillMaxSize()) {
        val characterItems = uiState.value.gridItems

        Spacer(Modifier.height(48.dp))
        Text(
            text = stringResource(Res.string.characters_screen_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text =
                stringResource(
                    Res.string.characters_screen_subtitle,
                    characterItems.filter { it is HiraganaCharacterOnGrid.Filled }.size,
                    HiraganaCharacter.entries.size,
                ),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            content = {
                items(characterItems) { gridItem ->
                    Box(
                        modifier =
                            Modifier
                                .background(
                                    color =
                                        if (gridItem is HiraganaCharacterOnGrid.Null) {
                                            Color.LightGray
                                        } else {
                                            Color.Transparent
                                        },
                                ).border(width = 0.5.dp, Color.Gray)
                                .fillMaxSize(),
                    ) {
                        Column(Modifier.fillMaxSize()) {
                            Text(
                                text =
                                    if (gridItem is HiraganaCharacterOnGrid.Filled) {
                                        gridItem.character.spelling.toString()
                                    } else {
                                        ""
                                    },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                            )
                            Text(
                                text =
                                    if (gridItem is HiraganaCharacterOnGrid.Filled) {
                                        gridItem.character.pronunciation.toString()
                                    } else {
                                        ""
                                    },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                            )
                        }
                    }
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(width = 1.dp, Color.Gray),
        )
    }
}
