package com.sechkarev.hiraganateacherkmp.domain

import com.sechkarev.hiraganateacherkmp.database.ChallengeSolutionDao
import com.sechkarev.hiraganateacherkmp.database.ChallengeSolutionEntity
import com.sechkarev.hiraganateacherkmp.model.Challenge
import com.sechkarev.hiraganateacherkmp.model.GameProgress
import com.sechkarev.hiraganateacherkmp.model.SolvedChallenge
import com.sechkarev.hiraganateacherkmp.model.Stroke
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class GameRepository(
    private val challengeSolutionDao: ChallengeSolutionDao,
) {
    val gameProgress: Flow<GameProgress> =
        challengeSolutionDao
            .retrieveAllSolutions()
            .map { solutions ->
                GameProgress(
                    solvedChallenges =
                        solutions.map { challengeSolution ->
                            SolvedChallenge(
                                challenge = Challenge.entries.first { it.name == challengeSolution.challengeId },
                                solution = Json.decodeFromString(challengeSolution.solution),
                            )
                        },
                    currentChallenge =
                        if (solutions.size < Challenge.entries.size) {
                            Challenge.entries[solutions.size]
                        } else {
                            null
                        },
                    gameCompleted = solutions.size >= Challenge.entries.size,
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

    suspend fun deleteAllSolutions() {
        challengeSolutionDao.deleteAllSolutions()
    }
}
