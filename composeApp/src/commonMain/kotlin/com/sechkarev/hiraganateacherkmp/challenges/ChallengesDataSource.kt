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
import com.sechkarev.hiraganateacherkmp.model.UiComponent.Image
import com.sechkarev.hiraganateacherkmp.model.UiComponent.NewWord
import com.sechkarev.hiraganateacherkmp.model.UiComponent.Text
import kmphiraganateacher.composeapp.generated.resources.Res
import kmphiraganateacher.composeapp.generated.resources.blue_house
import kmphiraganateacher.composeapp.generated.resources.challenge10_text
import kmphiraganateacher.composeapp.generated.resources.challenge11_text
import kmphiraganateacher.composeapp.generated.resources.challenge12_task
import kmphiraganateacher.composeapp.generated.resources.challenge12_text
import kmphiraganateacher.composeapp.generated.resources.challenge13_task
import kmphiraganateacher.composeapp.generated.resources.challenge14_task
import kmphiraganateacher.composeapp.generated.resources.challenge15_task
import kmphiraganateacher.composeapp.generated.resources.challenge16_task
import kmphiraganateacher.composeapp.generated.resources.challenge16_text
import kmphiraganateacher.composeapp.generated.resources.challenge17_task
import kmphiraganateacher.composeapp.generated.resources.challenge18_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge18_task
import kmphiraganateacher.composeapp.generated.resources.challenge19_task
import kmphiraganateacher.composeapp.generated.resources.challenge1_task
import kmphiraganateacher.composeapp.generated.resources.challenge1_text
import kmphiraganateacher.composeapp.generated.resources.challenge1_welcome_message
import kmphiraganateacher.composeapp.generated.resources.challenge2_task
import kmphiraganateacher.composeapp.generated.resources.challenge3_task
import kmphiraganateacher.composeapp.generated.resources.challenge3_text
import kmphiraganateacher.composeapp.generated.resources.challenge4_task
import kmphiraganateacher.composeapp.generated.resources.challenge7_task
import kmphiraganateacher.composeapp.generated.resources.challenge8_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge8_task
import kmphiraganateacher.composeapp.generated.resources.challenge9_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge9_task
import kmphiraganateacher.composeapp.generated.resources.challenge_aoi_task
import kmphiraganateacher.composeapp.generated.resources.challenge_blue_house_remainder
import kmphiraganateacher.composeapp.generated.resources.challenge_blue_house_task
import kmphiraganateacher.composeapp.generated.resources.challenge_blue_house_text
import kmphiraganateacher.composeapp.generated.resources.challenge_hae_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge_hae_task
import kmphiraganateacher.composeapp.generated.resources.challenge_i_no_hint_task
import kmphiraganateacher.composeapp.generated.resources.challenge_ii_congratulations
import kmphiraganateacher.composeapp.generated.resources.challenge_ii_task
import kmphiraganateacher.composeapp.generated.resources.challenge_o_introduction_task
import kmphiraganateacher.composeapp.generated.resources.challenge_o_introduction_text
import kmphiraganateacher.composeapp.generated.resources.challenge_o_repetition_task
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ai
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ao_blue
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ao_green
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_aoi_blue
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_aoi_green
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_hae
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_hai
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ie
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_ii
import kmphiraganateacher.composeapp.generated.resources.dictionary_word_iie
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_a
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_e
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_ha
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_i
import kmphiraganateacher.composeapp.generated.resources.hiragana_static_o
import kmphiraganateacher.composeapp.generated.resources.picture_description_blue_house
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.jetbrains.compose.resources.DrawableResource
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
        hiraganaCharacters = parseHiraganaCharacters()
        challengeAnswers = parseChallengeAnswers()
        dictionaryItems = parseDictionaryItems()
        challenges = parseChallenges()

        Logger.i(null, "ChallengesDataSource") { dictionaryItems.joinToString() }
        initialised = true
    }

    fun getRequiredStrokesNumber(challengeAnswer: ChallengeAnswer) =
        challengeAnswer
            .answerText
            .map { answerCharacter ->
                hiraganaCharacters.first { it.spelling == answerCharacter }.requiredStrokes
            }.sum()

    private suspend fun parseChallenges(): List<Challenge> {
        val module =
            SerializersModule {
                polymorphic(baseClass = UiComponentDto::class) {
                    subclass(HeadlineUiComponentDto::class)
                    subclass(TextUiComponentDto::class)
                    subclass(AnimationUiComponentDto::class)
                    subclass(NewWordUiComponentDto::class)
                    subclass(DrawingChallengeUiComponentDto::class)
                    subclass(ImageUiComponentDto::class)
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
                            dictionaryItems.first { challengeDto.dictionaryItem == it.name }
                        },
                    newCharacter =
                        if (challengeDto.newCharacter == null) {
                            null
                        } else {
                            hiraganaCharacters.first { challengeDto.newCharacter == it.name }
                        },
                    secondsToComplete = challengeDto.secondsToComplete,
                    uiComponents =
                        challengeDto.uiComponents.map { uiComponent ->
                            when (uiComponent) {
                                is HeadlineUiComponentDto ->
                                    Headline(
                                        textResource = mapHeadlineConfigIdToStringResource(uiComponent.properties.textId),
                                    )
                                is AnimationUiComponentDto ->
                                    Animation(
                                        animationId = mapAnimationConfigIdToFileName(uiComponent.properties.animationId),
                                    )
                                is DrawingChallengeUiComponentDto ->
                                    DrawingChallenge(
                                        hintResource = uiComponent.properties.hint?.let { mapHintConfigIdToDrawableResource(it) },
                                        decoration =
                                            if (uiComponent.properties.canvasDecoration == null) {
                                                null
                                            } else {
                                                CanvasDecoration.entries.first { it.configKey == uiComponent.properties.canvasDecoration }
                                            },
                                    )
                                is NewWordUiComponentDto ->
                                    NewWord(
                                        word = dictionaryItems.first { it.name == uiComponent.properties.word },
                                    )
                                is TextUiComponentDto ->
                                    Text(
                                        textResource = mapTextConfigIdToStringResource(uiComponent.properties.textId),
                                    )
                                is ImageUiComponentDto ->
                                    Image(
                                        link = uiComponent.properties.link,
                                        courtesy = uiComponent.properties.courtesy,
                                        imageResource = mapImageIdToDrawableResource(uiComponent.properties.imageId),
                                        descriptionResource = mapImageIdToDescriptionResource(uiComponent.properties.imageId),
                                    )
                            }
                        },
                )
            }
    }

    private fun mapHeadlineConfigIdToStringResource(headlineId: String): StringResource =
        when (headlineId) {
            "i_beginning_introduction" -> Res.string.challenge1_welcome_message
            else -> throw IllegalArgumentException("Can't find a string corresponding with the headline id $headlineId")
        }

    private fun mapAnimationConfigIdToFileName(animationId: String): String =
        when (animationId) {
            "i_introduction_animation" -> "hiragana_animated_i"
            "e_introduction_animation" -> "hiragana_animated_e"
            "ha_introduction_animation" -> "hiragana_animated_ha"
            "a_introduction_animation" -> "hiragana_animated_a"
            "o_introduction_animation" -> "hiragana_animated_o"
            else -> throw IllegalArgumentException("Can't find a string corresponding with the animation id $animationId")
        }

    private fun mapTextConfigIdToStringResource(textId: String): StringResource =
        when (textId) {
            "i_beginning_explanation" -> Res.string.challenge1_text // todo: remove numbers from ids
            "i_beginning_task" -> Res.string.challenge1_task
            "i_repetition_task" -> Res.string.challenge2_task
            "i_no_hint_task" -> Res.string.challenge_i_no_hint_task
            "new_word_ii_task" -> Res.string.challenge_ii_task
            "new_word_ii_congratulations" -> Res.string.challenge_ii_congratulations
            "e_introduction_text" -> Res.string.challenge3_text
            "e_introduction_task" -> Res.string.challenge3_task
            "e_repetition_task" -> Res.string.challenge4_task
            "new_word_ie_task" -> Res.string.challenge7_task
            "new_word_ie_congratulations" -> Res.string.challenge8_congratulations
            "new_word_iie_task" -> Res.string.challenge8_task
            "new_word_iie_congratulations" -> Res.string.challenge9_congratulations
            "are_you_afraid_task" -> Res.string.challenge9_task
            "iie_timed_10_sec_task" -> Res.string.challenge10_text
            "iie_timed_5_sec_task" -> Res.string.challenge11_text
            "ha_introduction_text" -> Res.string.challenge12_text
            "ha_introduction_task" -> Res.string.challenge12_task
            "ha_repetition_task" -> Res.string.challenge13_task
            "new_word_hai_task" -> Res.string.challenge14_task
            "hai_timed_5_sec_task" -> Res.string.challenge15_task
            "new_word_hae_task" -> Res.string.challenge_hae_task
            "new_word_hae_congratulations" -> Res.string.challenge_hae_congratulations
            "a_introduction_text" -> Res.string.challenge16_text
            "a_introduction_task" -> Res.string.challenge16_task
            "a_repetition_task" -> Res.string.challenge17_task
            "new_word_ai_congratulations" -> Res.string.challenge18_congratulations
            "new_word_ai_task" -> Res.string.challenge18_task
            "ai_decorated_canvas_task" -> Res.string.challenge19_task
            "o_introduction_text" -> Res.string.challenge_o_introduction_text
            "o_introduction_task" -> Res.string.challenge_o_introduction_task
            "o_repetition_task" -> Res.string.challenge_o_repetition_task
            "new_word_aoi_task" -> Res.string.challenge_aoi_task
            "blue_house_text" -> Res.string.challenge_blue_house_text
            "blue_house_task" -> Res.string.challenge_blue_house_task
            "blue_house_remainder" -> Res.string.challenge_blue_house_remainder
            else -> throw IllegalArgumentException("Can't find a string corresponding with the id $textId")
        }

    private fun mapHintConfigIdToDrawableResource(hintId: String): DrawableResource =
        when (hintId) {
            "a_introduction_image" -> Res.drawable.hiragana_static_a
            "o_introduction_image" -> Res.drawable.hiragana_static_o
            "i_introduction_image" -> Res.drawable.hiragana_static_i
            "e_introduction_image" -> Res.drawable.hiragana_static_e
            "ha_introduction_image" -> Res.drawable.hiragana_static_ha
            else -> throw IllegalArgumentException("Can't find a drawable corresponding with the hint id $hintId")
        }

    private fun mapImageIdToDrawableResource(imageId: String): DrawableResource =
        when (imageId) {
            "blue_house_image" -> Res.drawable.blue_house
            else -> throw IllegalArgumentException("Can't find a drawable corresponding with the image id $imageId")
        }

    private fun mapImageIdToDescriptionResource(imageId: String): StringResource =
        when (imageId) {
            "blue_house_image" -> Res.string.picture_description_blue_house
            else -> throw IllegalArgumentException("Can't find a drawable corresponding with the image id $imageId")
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
                    spelling = it.spelling.first(), // should only be one character
                    pronunciation = it.pronunciation,
                    gridCell = it.gridCell,
                    requiredStrokes = it.requiredStrokes,
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
                )
            }
}

@Serializable
private data class ChallengeAnswerDto(
    val name: String,
    val answerText: String,
)

@Serializable
private data class HiraganaCharacterDto(
    val name: String,
    val spelling: String,
    val pronunciation: String,
    val gridCell: Int,
    val requiredStrokes: Int,
)

@Serializable
private data class DictionaryItemDto(
    val name: String,
    val original: String,
) {
    val translation: StringResource
        get() =
            when (name) {
                "II" -> Res.string.dictionary_word_ii
                "IE" -> Res.string.dictionary_word_ie
                "IIE" -> Res.string.dictionary_word_iie
                "HAI" -> Res.string.dictionary_word_hai
                "HAE" -> Res.string.dictionary_word_hae
                "AI" -> Res.string.dictionary_word_ai
                "AO_green" -> Res.string.dictionary_word_ao_green
                "AO_blue" -> Res.string.dictionary_word_ao_blue
                "AOI_green" -> Res.string.dictionary_word_aoi_green
                "AOI_blue" -> Res.string.dictionary_word_aoi_blue
                else -> throw IllegalArgumentException("Can't find a dictionary item corresponding with the name $name")
            }
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
@SerialName("image")
private data class ImageUiComponentDto(
    val properties: ImageUiComponentProperties,
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
private data class ImageUiComponentProperties(
    val imageId: String,
    val link: String,
    val courtesy: String,
)

@Serializable
private data class DrawingChallengeUiComponentProperties(
    val hint: String? = null,
    val canvasDecoration: String? = null,
)
