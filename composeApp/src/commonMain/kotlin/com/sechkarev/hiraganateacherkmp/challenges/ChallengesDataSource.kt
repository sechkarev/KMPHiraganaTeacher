package com.sechkarev.hiraganateacherkmp.challenges

import co.touchlab.kermit.Logger
import com.sechkarev.hiraganateacherkmp.model.CanvasDecoration
import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.ChallengeAnswer
import com.sechkarev.hiraganateacherkmp.model.DictionaryItem
import com.sechkarev.hiraganateacherkmp.model.HiraganaCharacter
import com.sechkarev.hiraganateacherkmp.model.UiComponent.Animation
import com.sechkarev.hiraganateacherkmp.model.UiComponent.DrawingChallenge
import com.sechkarev.hiraganateacherkmp.model.UiComponent.Headline
import com.sechkarev.hiraganateacherkmp.model.UiComponent.NewWord
import com.sechkarev.hiraganateacherkmp.model.UiComponent.Text
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.challenge1_welcome_message
import kmphiraganateacher.composeapp.generated.resources.challenge3_text
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ai
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_e
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
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
        challengeAnswers = parseChallengeAnswers()
        hiraganaCharacters = parseHiraganaCharacters()
        dictionaryItems = parseDictionaryItems()
        challenges = parseChallenges()

        Logger.i(null, "ChallengesDataSource") { challenges.joinToString() }
        initialised = true
    }

    private suspend fun parseChallenges(): List<Challenge> {
        val module =
            SerializersModule {
                polymorphic(baseClass = UiComponentDto::class) {
                    subclass(HeadlineUiComponentDto::class)
                    subclass(TextUiComponentDto::class)
                    subclass(AnimationUiComponentDto::class)
                    subclass(NewWordUiComponentDto::class)
                    subclass(DrawingChallengeUiComponentDto::class)
                }
            }
        val json =
            Json {
                serializersModule = module
                classDiscriminator = "type"
                ignoreUnknownKeys = true // Safe for forward compatibility
            }
        return json
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
                    uiComponents =
                        challengeDto.uiComponents.map { uiComponent ->
                            when (uiComponent) {
                                is HeadlineUiComponentDto ->
                                    Headline(
                                        textResource = Res.string.challenge1_welcome_message, // todo: proper mapping!!!
                                    )
                                is AnimationUiComponentDto ->
                                    Animation(
                                        animationId = uiComponent.properties.animationId,
                                    )
                                is DrawingChallengeUiComponentDto ->
                                    DrawingChallenge(
                                        hintResource = Res.drawable.hiragana_static_e, // todo: proper mapping
                                        decoration = CanvasDecoration.HEARTS, // todo: proper mapping
                                    )
                                is NewWordUiComponentDto ->
                                    NewWord(
                                        word = dictionaryItems.first { it.name == uiComponent.properties.word },
                                    )
                                is TextUiComponentDto ->
                                    Text(
                                        textResource = Res.string.challenge3_text, // todo: proper mapping!
                                    )
                            }
                        },
                )
            }
    }

    private suspend fun parseDictionaryItems(): List<DictionaryItem> =
        Json
            .decodeFromString<List<DictionaryItemDto>>(
                Res.readBytes("files/dictionary_items.json").decodeToString(),
            ).map {
                DictionaryItem(
                    name = it.name,
                    original = it.original,
                    translation = it.translation,
                )
            }

    private suspend fun parseHiraganaCharacters(): List<HiraganaCharacter> =
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

    private suspend fun parseChallengeAnswers(): List<ChallengeAnswer> =
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
}

@Serializable
private data class ChallengeAnswerDto(
    val name: String,
    val answerText: String,
    val requiredStrokes: Int,
)

@Serializable
private data class HiraganaCharacterDto(
    val name: String,
    val spelling: String,
    val pronunciation: String,
    val gridCell: Int,
)

@Serializable
private data class DictionaryItemDto(
    val name: String,
    val original: String,
) {
    val translation: StringResource
        get() = Res.string.dictionary_word_ai // fixme: proper algorithm!!
}

@Serializable
private data class ChallengeDto(
    val name: String,
    val answer: String,
    val uiComponents: List<@Polymorphic UiComponentDto>,
    val dictionaryItem: String? = null,
    val newCharacter: String? = null,
    val secondsToComplete: Int? = null,
)

@Serializable
private sealed class UiComponentDto

@Serializable
@SerialName("headline")
private data class HeadlineUiComponentDto(
    val properties: HeadlineUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("text")
private data class TextUiComponentDto(
    val properties: TextUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("animation")
private data class AnimationUiComponentDto(
    val properties: AnimationUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("new_word")
private data class NewWordUiComponentDto(
    val properties: NewWordUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("drawing_challenge")
private data class DrawingChallengeUiComponentDto(
    val properties: DrawingChallengeUiComponentProperties,
) : UiComponentDto()

@Serializable
private data class HeadlineUiComponentProperties(
    val textId: String,
)

@Serializable
private data class TextUiComponentProperties(
    val textId: String,
)

@Serializable
private data class AnimationUiComponentProperties(
    val animationId: String,
)

@Serializable
private data class NewWordUiComponentProperties(
    val word: String,
)

@Serializable
private data class DrawingChallengeUiComponentProperties(
    val hint: String? = null,
    val canvasDecoration: String? = null,
)
