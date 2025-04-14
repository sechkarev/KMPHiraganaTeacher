package com.sechkarev.hiraganateacherkmp.model

data class GameProgress(
    val solvedChallenges: List<SolvedChallenge> = emptyList(),
    val currentChallenge: Challenge? = null,
    val gameCompleted: Boolean = false,
)
