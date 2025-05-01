package com.sechkarev.hiraganateacherkmp.tts

expect interface TextToSpeechEngine {
    fun initialise(onCompletion: () -> Unit)

    fun pronounce(text: String)
}
