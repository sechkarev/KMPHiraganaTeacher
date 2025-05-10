package com.sechkarev.hiraganateacherkmp.domain

import com.sechkarev.hiraganateacherkmp.challenges.ChallengesDataSource
import com.sechkarev.hiraganateacherkmp.database.ChallengeSolutionDao
import com.sechkarev.hiraganateacherkmp.database.ChallengeSolutionEntity
import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.GameProgress
import com.sechkarev.hiraganateacherkmp.model.SolvedChallenge
import com.sechkarev.hiraganateacherkmp.model.Stroke
import kotlinx.serialization.json.Json

class GameRepository(
    private val challengeSolutionDao: ChallengeSolutionDao,
    private val challengesDataSource: ChallengesDataSource,
) {
    suspend fun retrieveGameProgress(): GameProgress {
        val solutions = challengeSolutionDao.retrieveAllSolutions()
        return GameProgress(
            solvedChallenges =
                solutions.map { challengeSolution ->
                    SolvedChallenge(
                        challenge = challengesDataSource.challenges.first { it.name == challengeSolution.challengeId },
                        solution = Json.decodeFromString(challengeSolution.solution),
                    )
                },
            currentChallenge =
                if (solutions.size < challengesDataSource.challenges.size) {
                    challengesDataSource.challenges[solutions.size]
                } else {
                    null
                },
            gameCompleted = solutions.size >= challengesDataSource.challenges.size,
        )
    }

    suspend fun insertSolution(
        challengeId: String,
        solution: List<Stroke>,
    ) {
        challengeSolutionDao.insertSolution(
            ChallengeSolutionEntity(
                challengeId = challengeId,
                solution = Json.encodeToString(solution),
            ),
        )
    }

    fun retrieveNextChallenge(currentChallengeId: String): Challenge? {
        val indexOfCurrentChallenge = challengesDataSource.challenges.indexOfFirst { it.name == currentChallengeId }
        return if (indexOfCurrentChallenge == challengesDataSource.challenges.lastIndex) {
            null
        } else {
            challengesDataSource.challenges[indexOfCurrentChallenge + 1]
        }
    }

    suspend fun deleteAllSolutions() {
        challengeSolutionDao.deleteAllSolutions()
    }
}
