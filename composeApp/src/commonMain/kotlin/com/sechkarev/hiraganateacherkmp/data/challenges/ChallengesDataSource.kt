package com.sechkarev.hiraganateacherkmp.data.challenges

import kmphiraganateacher.composeapp.generated.resources.Res
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

// todo: compose resources are used, while this class is in the data layer and shouldn't know about compose
class ChallengesDataSource {
    suspend fun parseDictionaryItems(): List<DictionaryItemDto> =
        Json
            .decodeFromString<List<DictionaryItemDto>>(
                Res.readBytes("files/dictionary_items.json").decodeToString(),
            )

    suspend fun parseHiraganaCharacters(): List<HiraganaCharacterDto> =
        Json
            .decodeFromString<List<HiraganaCharacterDto>>(
                Res.readBytes("files/hiragana_characters.json").decodeToString(),
            )

    suspend fun parseChallengeAnswers(): List<ChallengeAnswerDto> =
        Json
            .decodeFromString<List<ChallengeAnswerDto>>(
                Res.readBytes("files/challenge_answers.json").decodeToString(),
            )

    suspend fun parseChallenges(): List<ChallengeDto> {
        val module =
            SerializersModule {
                polymorphic(baseClass = UiComponentDto::class) {
                    subclass(HeadlineUiComponentDto::class)
                    subclass(TextUiComponentDto::class)
                    subclass(AnimationUiComponentDto::class)
                    subclass(NewWordUiComponentDto::class)
                    subclass(DrawingChallengeUiComponentDto::class)
                    subclass(ImageUiComponentDto::class)
                    subclass(ConditionalTextUiComponentDto::class)
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
            )
    }
}

@Serializable
data class ChallengeAnswerDto(
    val name: String,
    val answerText: String,
)

@Serializable
data class HiraganaCharacterDto(
    val name: String,
    val spelling: String,
    val pronunciation: String,
    val gridCell: Int,
    val requiredStrokes: Int,
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
    val uiComponents: List<@Polymorphic UiComponentDto>,
    val dictionaryItem: String? = null,
    val newCharacter: String? = null,
    val secondsToComplete: Int? = null,
)

@Serializable
sealed class UiComponentDto

@Serializable
@SerialName("headline")
data class HeadlineUiComponentDto(
    val properties: HeadlineUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("text")
data class TextUiComponentDto(
    val properties: TextUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("animation")
data class AnimationUiComponentDto(
    val properties: AnimationUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("new_word")
data class NewWordUiComponentDto(
    val properties: NewWordUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("drawing_challenge")
data class DrawingChallengeUiComponentDto(
    val properties: DrawingChallengeUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("image")
data class ImageUiComponentDto(
    val properties: ImageUiComponentProperties,
) : UiComponentDto()

@Serializable
@SerialName("conditional_text")
data class ConditionalTextUiComponentDto(
    val properties: ConditionalTextUiComponentProperties,
) : UiComponentDto()

@Serializable
data class ConditionalTextUiComponentProperties(
    val trueTextId: String,
    val falseTextId: String,
)

@Serializable
data class HeadlineUiComponentProperties(
    val textId: String,
)

@Serializable
data class TextUiComponentProperties(
    val textId: String,
)

@Serializable
data class AnimationUiComponentProperties(
    val animationId: String,
)

@Serializable
data class NewWordUiComponentProperties(
    val word: String,
)

@Serializable
data class ImageUiComponentProperties(
    val imageId: String,
    val link: String,
    val courtesy: String,
)

@Serializable
data class DrawingChallengeUiComponentProperties(
    val hint: String? = null,
    val canvasDecoration: String? = null,
)
