package com.sechkarev.hiraganateacherkmp.challenges

import co.touchlab.kermit.Logger
import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.ChallengeAnswer
import com.sechkarev.hiraganateacherkmp.model.DictionaryItem
import com.sechkarev.hiraganateacherkmp.model.HiraganaCharacter
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ai
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.StringResource

// todo: compose resources are used, while this class is in the data layer and shouldn't know about compose
class ChallengesDataSource {
    private var initialised = false

    lateinit var challenges: List<Challenge>
        private set
    lateinit var hiraganaCharacters: List<HiraganaCharacter>
        private set
    lateinit var challengeAnswers: List<ChallengeAnswer>
        private set
    lateinit var dictionaryItems: List<DictionaryItem>
        private set

    suspend fun init() {
        if (initialised) {
            return
        }
        challengeAnswers =
            Json
                .decodeFromString<List<ChallengeAnswerDto>>(
                    Res.readBytes("files/challenge_answers.json").decodeToString(),
                ).map {
                    ChallengeAnswer(
                        name = it.name,
                        answerText = it.answerText,
                        requiredStrokes = it.requiredStrokes,
                    )
                }
        hiraganaCharacters =
            Json
                .decodeFromString<List<HiraganaCharacterDto>>(
                    Res.readBytes("files/hiragana_characters.json").decodeToString(),
                ).map {
                    HiraganaCharacter(
                        name = it.name,
                        spelling = it.spelling,
                        pronunciation = it.pronunciation,
                        gridCell = it.gridCell,
                    )
                }
        dictionaryItems =
            Json
                .decodeFromString<List<DictionaryItemDto>>(
                    Res.readBytes("files/dictionary_items.json").decodeToString(),
                ).map {
                    DictionaryItem(
                        it.name,
                        it.original,
                        it.translation,
                    )
                }
        challenges =
            Json
                .decodeFromString<List<ChallengeDto>>(
                    Res.readBytes("files/challenges.json").decodeToString(),
                ).map { challengeDto ->
                    Challenge(
                        name = challengeDto.name,
                        challengeAnswer = challengeAnswers.first { challengeDto.answer == it.name },
                        dictionaryItem =
                            if (challengeDto.dictionaryItem == null) {
                                null
                            } else {
                                dictionaryItems.firstOrNull { challengeDto.dictionaryItem == it.name }
                            },
                        newCharacter =
                            if (challengeDto.newCharacter == null) {
                                null
                            } else {
                                hiraganaCharacters.firstOrNull { challengeDto.newCharacter == it.name }
                            },
                        secondsToComplete = challengeDto.secondsToComplete,
                    )
                }
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
) {
    val translation: StringResource
        get() = Res.string.dictionary_word_ai // fixme: proper algorithm!!
}

@Serializable
data class ChallengeDto(
    val name: String,
    val answer: String,
    val dictionaryItem: String? = null,
    val newCharacter: String? = null,
    val secondsToComplete: Int? = null,
)
