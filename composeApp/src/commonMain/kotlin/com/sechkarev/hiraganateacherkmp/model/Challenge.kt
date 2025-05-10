package com.sechkarev.hiraganateacherkmp.model

import org.jetbrains.compose.resources.StringResource

data class SolvedChallenge(
    val challenge: Challenge,
    val solution: List<Stroke>,
)

data class ChallengeAnswer(
    val name: String,
    val answerText: String,
    val requiredStrokes: Int,
)

data class DictionaryItem(
    val name: String,
    val original: String,
    val translation: StringResource,
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
    val spelling: String,
    val pronunciation: String,
    val gridCell: Int,
)

data class Challenge(
    val name: String,
    val challengeAnswer: ChallengeAnswer,
    val dictionaryItem: DictionaryItem? = null,
    val newCharacter: HiraganaCharacter? = null,
    val secondsToComplete: Int? = null,
)
