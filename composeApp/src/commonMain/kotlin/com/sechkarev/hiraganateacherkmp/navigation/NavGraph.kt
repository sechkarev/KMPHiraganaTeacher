package com.sechkarev.hiraganateacherkmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sechkarev.hiraganateacherkmp.ui.characters.CharacterListScreen
import kotlinx.serialization.Serializable

@Serializable
private data object MainMenu

@Serializable
private data object Credits

@Serializable
private data object Game

@Serializable
private data object Dictionary

@Serializable
private data object CharacterList

@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = CharacterList,
        builder = {
            /**
             composable<MainMenu> {
             MainMenuScreen(
             onStartGameClick = {
             navController.navigate(Game)
             },
             onCreditsClick = {
             navController.navigate(Credits)
             },
             onDictionaryClick = {
             navController.navigate(Dictionary)
             },
             onCharacterListClick = {
             navController.navigate(CharacterList)
             },
             )
             }
             composable<Credits> {
             CreditsScreen()
             }
             composable<Game> {
             GameScreen(
             onReturnToMainMenuClick = {
             navController.navigateUp()
             },
             )
             }
             composable<Dictionary> {
             DictionaryScreen()
             }
             */
            composable<CharacterList> {
                CharacterListScreen()
            }
        },
    )
}
