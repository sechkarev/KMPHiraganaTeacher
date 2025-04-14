package com.sechkarev.hiraganateacherkmp.ui.characters

import androidx.lifecycle.ViewModel
import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.model.HiraganaCharacter
import com.sechkarev.hiraganateacherkmp.model.HiraganaCharacterOnGrid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterListViewModel(
    gameRepository: GameRepository,
) : ViewModel() {
    data class UiState(
        val gridItems: List<HiraganaCharacterOnGrid>,
    )

    val uiState: Flow<UiState> =
        gameRepository
            .gameProgress
            .map { gameProgress ->
                UiState(
                    putCharactersOnGrid(
                        HiraganaCharacter.entries,
                        // todo gameProgress.solvedChallenges.mapNotNull { it.challenge.newCharacter }
                    ),
                )
            }

    private fun putCharactersOnGrid(characters: List<HiraganaCharacter>): List<HiraganaCharacterOnGrid> {
        // Given that we know in advance how a hiragana grid typically looks like,
        // this is pretty much the most optimised approach I conceived.
        // It looks ugly, but always does the job in O(n)
        val result: MutableList<HiraganaCharacterOnGrid> = (0 until 50).map { HiraganaCharacterOnGrid.Empty }.toMutableList()
        // I could have added these 4 calls below to the list constructor, but this would result in more bad-looking code
        result[36] = HiraganaCharacterOnGrid.Null
        result[38] = HiraganaCharacterOnGrid.Null
        result[46] = HiraganaCharacterOnGrid.Null
        result[48] = HiraganaCharacterOnGrid.Null
        // Last step: put the characters we have already learned on the grid
        characters.forEach {
            result[it.gridCell] = HiraganaCharacterOnGrid.Filled(it)
        }
        return result
    }
}
