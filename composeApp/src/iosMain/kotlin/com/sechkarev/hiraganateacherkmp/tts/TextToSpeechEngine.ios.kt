package com.sechkarev.hiraganateacherkmp.tts

actual interface TextToSpeechEngine {
    actual fun pronounce(text: String)

    actual fun initialise(onCompletion: () -> Unit)
}

// todo: impl and inject
class TextToSpeechEngineImpl : TextToSpeechEngine {
    override fun initialise(onCompletion: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun pronounce(text: String) {
        TODO("Not yet implemented")
    }
}
