package com.sechkarev.hiraganateacherkmp.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import co.touchlab.kermit.Logger
import java.util.Locale
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

actual interface TextToSpeechEngine {
    actual fun pronounce(text: String)

    actual fun initialise(onCompletion: () -> Unit)
}

@OptIn(ExperimentalUuidApi::class)
class TextToSpeechEngineImpl(
    private val context: Context,
) : TextToSpeechEngine {
    private var textToSpeech: TextToSpeech? = null

    override fun initialise(onCompletion: () -> Unit) {
        textToSpeech =
            TextToSpeech(context) {
                textToSpeech?.language = Locale.JAPAN
                onCompletion()
            }
    }

    override fun pronounce(text: String) {
        Logger.i { "pronouncing $text, tts = $textToSpeech" }
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, Uuid.random().toString())
    }
}
