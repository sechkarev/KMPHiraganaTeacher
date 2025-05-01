package com.sechkarev.hiraganateacherkmp.tts

actual interface TextToSpeechEngine {
    actual fun pronounce(text: String)

    actual fun initialise(onCompletion: () -> Unit)
}
