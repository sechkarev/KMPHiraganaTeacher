package com.sechkarev.hiraganateacherkmp.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class SolvedChallenge(
    val challenge: Challenge,
    val solution: List<Stroke>,
)

data class ChallengeAnswer(
    val name: String,
    val answerText: String,
)

data class DictionaryItem(
    val name: String,
    val original: String,
    val translation: StringResource,
    val onlineDictionaryLink: StringResource,
)

sealed interface HiraganaCharacterOnGrid {
    data class Filled(
        val character: HiraganaCharacter,
    ) : HiraganaCharacterOnGrid

    data object Empty : HiraganaCharacterOnGrid

    data object Null : HiraganaCharacterOnGrid
}

data class HiraganaCharacter(
    val name: String,
    val spelling: Char,
    val pronunciation: String,
    val gridCell: Int,
    val requiredStrokes: Int,
)

data class Challenge(
    val name: String,
    val challengeAnswer: ChallengeAnswer,
    val uiComponents: List<UiComponent>,
    val dictionaryItem: DictionaryItem? = null,
    val newCharacter: HiraganaCharacter? = null,
    val secondsToComplete: Int? = null,
)

enum class CanvasDecoration(
    val configKey: String,
) {
    HEARTS("hearts"),
}

sealed interface UiComponent {
    data class Headline(
        val textResource: StringResource,
    ) : UiComponent

    data class Text(
        val textResource: StringResource,
    ) : UiComponent

    data class Animation(
        val animationId: String, // todo: create a wrapper?.. idk
    ) : UiComponent

    data class NewWord(
        val word: DictionaryItem,
    ) : UiComponent

    data class DrawingChallenge(
        val hintResource: DrawableResource? = null,
        val decoration: CanvasDecoration? = null,
    ) : UiComponent

    data class Image(
        val imageResource: DrawableResource,
        val link: String,
        val courtesy: String,
        val descriptionResource: StringResource,
    ) : UiComponent
}
