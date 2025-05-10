package com.sechkarev.hiraganateacherkmp.challenges

import co.touchlab.kermit.Logger
import kmphiraganateacher.composeapp.generated.resources.Res

// todo: compose resources are used, while this class is in the data layer and shouldn't know about compose
class ChallengesDataSource {
    suspend fun init() {
        val challengeAnswers = Res.readBytes("files/challenge_answers.json").decodeToString()
        Logger.i { challengeAnswers }
    }
}
