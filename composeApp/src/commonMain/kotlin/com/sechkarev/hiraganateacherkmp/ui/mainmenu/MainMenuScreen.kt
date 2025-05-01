package com.sechkarev.hiraganateacherkmp.ui.mainmenu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sechkarev.hiraganateacherkmp.utils.LengthyTask
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.app_name
import kmphiraganateacher.composeapp.generated.resources.app_name_japanese
import kmphiraganateacher.composeapp.generated.resources.main_menu_character_list_button_text
import kmphiraganateacher.composeapp.generated.resources.main_menu_content_loading_error
import kmphiraganateacher.composeapp.generated.resources.main_menu_content_loading_try_again
import kmphiraganateacher.composeapp.generated.resources.main_menu_content_loading_warning
import kmphiraganateacher.composeapp.generated.resources.main_menu_continue_game_button_text
import kmphiraganateacher.composeapp.generated.resources.main_menu_credits_button_text
import kmphiraganateacher.composeapp.generated.resources.main_menu_delete_game_data_button_text
import kmphiraganateacher.composeapp.generated.resources.main_menu_dictionary_button_text
import kmphiraganateacher.composeapp.generated.resources.main_menu_snackbar_game_data_deleted
import kmphiraganateacher.composeapp.generated.resources.main_menu_start_game_button_text
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainMenuScreen(
    onStartGameClick: () -> Unit,
    onDictionaryClick: () -> Unit,
    onCharacterListClick: () -> Unit,
    onCreditsClick: () -> Unit,
    modifier: Modifier = Modifier,
    mainMenuViewModel: MainMenuViewModel = koinViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarText = stringResource(Res.string.main_menu_snackbar_game_data_deleted)

    val mainMenuState by mainMenuViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        content = { innerPadding ->
            val textRecognitionInitResult = mainMenuState.textRecognitionInitResult
            Column(Modifier.padding(innerPadding).fillMaxSize()) {
                Spacer(Modifier.height(72.dp))
                Text(
                    text = stringResource(Res.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Text(
                    text = stringResource(Res.string.app_name_japanese),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.height(48.dp))
                Button(
                    onClick = onStartGameClick,
                    enabled = textRecognitionInitResult is LengthyTask.Success,
                    content = {
                        Text(
                            text =
                                stringResource(
                                    if (mainMenuState.progressHasBeenMade) {
                                        Res.string.main_menu_continue_game_button_text
                                    } else {
                                        Res.string.main_menu_start_game_button_text
                                    },
                                ),
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    enabled = mainMenuState.progressHasBeenMade,
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(snackbarText)
                        }
                        mainMenuViewModel.onAction(MainMenuViewModel.UiAction.DeleteGameData)
                    },
                    content = {
                        Text(text = stringResource(Res.string.main_menu_delete_game_data_button_text))
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                if (mainMenuState.dictionaryAvailable) {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = onDictionaryClick,
                        content = {
                            Text(text = stringResource(Res.string.main_menu_dictionary_button_text))
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
                if (mainMenuState.characterListAvailable) {
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = onCharacterListClick,
                        content = {
                            Text(text = stringResource(Res.string.main_menu_character_list_button_text))
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = onCreditsClick,
                    content = {
                        Text(text = stringResource(Res.string.main_menu_credits_button_text))
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.height(8.dp))
                if (textRecognitionInitResult is LengthyTask.InProgress) {
                    Text(
                        text = stringResource(Res.string.main_menu_content_loading_warning),
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally),
                    )
                }
                if (textRecognitionInitResult is LengthyTask.Error) {
                    Text(
                        text = stringResource(Res.string.main_menu_content_loading_error),
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier
                                .padding(horizontal = 16.dp)
                                .align(Alignment.CenterHorizontally),
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { mainMenuViewModel.onAction(MainMenuViewModel.UiAction.ReInitTextRecognizer) },
                        content = {
                            Text(text = stringResource(Res.string.main_menu_content_loading_try_again))
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
            }
        },
    )
}
