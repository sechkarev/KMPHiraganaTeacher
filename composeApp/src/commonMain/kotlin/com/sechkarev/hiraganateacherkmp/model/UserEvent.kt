package com.sechkarev.hiraganateacherkmp.model

sealed interface UserEvent {
    val name: String

    data object OpenDictionary : UserEvent {
        override val name: String
            get() = "open_dictionary"
    }

    data object ContinueGame : UserEvent {
        override val name: String
            get() = "continue_game"
    }

    data class BeatChallenge(
        val challengeId: String,
    ) : UserEvent {
        override val name: String
            get() = "beat_challenge_$challengeId"
    }
}
