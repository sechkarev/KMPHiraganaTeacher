package com.sechkarev.hiraganateacherkmp.ui.dictionary

import androidx.lifecycle.ViewModel
import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.model.DictionaryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DictionaryViewModel(
    gameRepository: GameRepository,
) : ViewModel() {
    data class UiState(
        val items: List<DictionaryItem>,
    )

    val uiState: Flow<UiState> =
        gameRepository
            .gameProgress
            .map { gameProgress ->
                UiState(gameProgress.solvedChallenges.mapNotNull { it.challenge.dictionaryItem })
            }
}
