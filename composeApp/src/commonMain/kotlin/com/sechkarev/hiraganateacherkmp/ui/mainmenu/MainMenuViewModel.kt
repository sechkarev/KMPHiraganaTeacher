package com.sechkarev.hiraganateacherkmp.ui.mainmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.textrecognition.TextRecognizer2
import com.sechkarev.hiraganateacherkmp.utils.LengthyTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val gameRepository: GameRepository,
    private val textRecognizer2: TextRecognizer2,
) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state =
        _state
            .onStart { loadGameContent() }
            .combine(gameRepository.gameProgress) { uiState, gameProgress ->
                uiState.copy(
                    progressHasBeenMade = gameProgress.solvedChallenges.isNotEmpty(),
                    dictionaryAvailable = gameProgress.solvedChallenges.mapNotNull { it.challenge.dictionaryItem }.isNotEmpty(),
                    characterListAvailable = gameProgress.solvedChallenges.mapNotNull { it.challenge.newCharacter }.isNotEmpty(),
                )
            }.stateIn(
                // I saw this approach in a Philipp Lackner's video and decided to give it a chance.
                // Works as intended, but if you ask me, the magic number doesn't belong here
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = UiState(),
            )

    data class UiState(
        val textRecognitionInitResult: LengthyTask<Unit> = LengthyTask.InProgress,
        val progressHasBeenMade: Boolean = false,
        val dictionaryAvailable: Boolean = false,
        val characterListAvailable: Boolean = false,
    )

    sealed interface UiAction {
        data object DeleteGameData : UiAction

        data object ReloadGameContent : UiAction
    }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.DeleteGameData -> onDeleteGameDataClick()
            UiAction.ReloadGameContent -> loadGameContent()
        }
    }

    private fun loadGameContent() {
        _state.update { it.copy(textRecognitionInitResult = LengthyTask.InProgress) }
        try {
            textRecognizer2.initialize(
                onSuccess = {
                    Logger.i { "textRecognizer2 init success" }
                    _state.update { it.copy(textRecognitionInitResult = LengthyTask.Success(Unit)) }
                },
                onFailure = {
                    Logger.i { "textRecognizer2 init failure" }
                    _state.update { it.copy(textRecognitionInitResult = LengthyTask.Error(Exception())) }
                },
            )
        } catch (throwable: Throwable) {
            _state.update { it.copy(textRecognitionInitResult = LengthyTask.Error(throwable)) }
        }
    }

    private fun onDeleteGameDataClick() {
        viewModelScope.launch {
            gameRepository.deleteAllSolutions()
        }
    }
}
