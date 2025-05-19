package com.sechkarev.hiraganateacherkmp.ui.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.model.DictionaryItem
import com.sechkarev.hiraganateacherkmp.model.UserEvent
import com.sechkarev.hiraganateacherkmp.ui.utils.stateInWhileSubscribed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class DictionaryViewModel(
    gameRepository: GameRepository,
) : ViewModel() {
    data class UiState(
        val items: List<DictionaryItem> = emptyList(),
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> =
        _uiState
            .onStart {
                gameRepository.logUserEvent(UserEvent.OpenDictionary)
                _uiState.update { UiState(gameRepository.getUnlockedDictionaryItems()) }
            }.stateInWhileSubscribed(viewModelScope, UiState())
}
