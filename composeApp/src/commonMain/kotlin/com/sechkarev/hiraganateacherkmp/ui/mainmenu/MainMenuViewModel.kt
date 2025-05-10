package com.sechkarev.hiraganateacherkmp.ui.mainmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sechkarev.hiraganateacherkmp.challenges.ChallengesDataSource
import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.textrecognition.TextRecognizer2
import com.sechkarev.hiraganateacherkmp.tts.TextToSpeechEngine
import com.sechkarev.hiraganateacherkmp.ui.utils.stateInWhileSubscribed
import com.sechkarev.hiraganateacherkmp.utils.LengthyTask
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

class MainMenuViewModel(
    private val gameRepository: GameRepository,
    private val textRecognizer: TextRecognizer2,
    private val textToSpeechEngine: TextToSpeechEngine,
    private val challengesDataSource: ChallengesDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state =
        _state
            .onStart {
                initData()
            }.combine(gameRepository.gameProgress) { uiState, gameProgress ->
                uiState.copy(
                    progressHasBeenMade = gameProgress.solvedChallenges.isNotEmpty(),
                    dictionaryAvailable = gameProgress.solvedChallenges.mapNotNull { it.challenge.dictionaryItem }.isNotEmpty(),
                    characterListAvailable = gameProgress.solvedChallenges.mapNotNull { it.challenge.newCharacter }.isNotEmpty(),
                )
            }.stateInWhileSubscribed(viewModelScope, UiState())

    data class UiState(
        val initResult: LengthyTask<Unit> = LengthyTask.InProgress,
        val progressHasBeenMade: Boolean = false,
        val dictionaryAvailable: Boolean = false,
        val characterListAvailable: Boolean = false,
    )

    sealed interface UiAction {
        data object DeleteGameData : UiAction

        data object RetryInit : UiAction
    }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.DeleteGameData -> onDeleteGameDataClick()
            UiAction.RetryInit -> initData() // todo: granular initialisation?
        }
    }

    // todo: this data is initialised every time the screen opens! I need to do it just once.
    private fun initData() {
        _state.update { it.copy(initResult = LengthyTask.InProgress) }
        viewModelScope.launch {
            val textRecognizerInitDeferred = async { initTextRecognizer() }
            val textToSpeechInitDeferred = async { initTextToSpeech() }
            val challengeRepositoryInitDeferred = async { challengesDataSource.init() }
            try {
                challengeRepositoryInitDeferred.await()
                textToSpeechInitDeferred.await()
                textRecognizerInitDeferred.await()
                _state.update { it.copy(initResult = LengthyTask.Success(Unit)) }
            } catch (throwable: Throwable) {
                _state.update { it.copy(initResult = LengthyTask.Error(throwable)) }
            }
        }
    }

    private suspend fun initTextRecognizer() =
        suspendCoroutine { continuation ->
            try {
                textRecognizer.initialize(
                    onSuccess = {
                        continuation.resumeWith(Result.success(Unit))
                    },
                    onFailure = {
                        continuation.resumeWith(Result.failure(Exception()))
                    },
                )
            } catch (throwable: Throwable) {
                continuation.resumeWith(Result.failure(throwable))
            }
        }

    private suspend fun initTextToSpeech() =
        suspendCoroutine { continuation ->
            try {
                textToSpeechEngine.initialise(
                    onCompletion = {
                        continuation.resumeWith(Result.success(Unit))
                    },
                )
            } catch (throwable: Throwable) {
                continuation.resumeWith(Result.failure(throwable))
            }
        }

    private fun onDeleteGameDataClick() {
        viewModelScope.launch {
            gameRepository.deleteAllSolutions()
        }
    }
}
