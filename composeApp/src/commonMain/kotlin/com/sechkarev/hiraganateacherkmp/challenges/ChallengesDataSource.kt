package com.sechkarev.hiraganateacherkmp.challenges

import co.touchlab.kermit.Logger
import kmphiraganateacher.composeapp.generated.resources.Res
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// todo: compose resources are used, while this class is in the data layer and shouldn't know about compose
class ChallengesDataSource {
    private var initialised = false

    suspend fun init() {
        if (initialised) {
            return
        }
        val challengeAnswers =
            Json.decodeFromString<List<ChallengeAnswerDto>>(
                Res.readBytes("files/challenge_answers.json").decodeToString(),
            )
        val hiraganaCharacters =
            Json.decodeFromString<List<HiraganaCharacterDto>>(
                Res.readBytes("files/hiragana_characters.json").decodeToString(),
            )
        val challenges =
            Json.decodeFromString<List<ChallengeDto>>(
                Res.readBytes("files/challenges.json").decodeToString(),
            )
        val dictionaryItems =
            Json.decodeFromString<List<DictionaryItemDto>>(
                Res.readBytes("files/dictionary_items.json").decodeToString(),
            )
        Logger.i { challengeAnswers.joinToString() }
        Logger.i { challenges.joinToString() }
        initialised = true
    }
}

@Serializable
data class ChallengeAnswerDto(
    val name: String,
    val answerText: String,
    val requiredStrokes: Int,
)

@Serializable
data class HiraganaCharacterDto(
    val name: String,
    val spelling: String,
    val pronunciation: String,
    val gridCell: Int,
)

@Serializable
data class DictionaryItemDto(
    val name: String,
    val original: String,
)

@Serializable
data class ChallengeDto(
    val name: String,
    val answer: String,
    val dictionaryItem: String? = null,
    val newCharacter: String? = null,
    val secondsToComplete: Int? = null,
)
