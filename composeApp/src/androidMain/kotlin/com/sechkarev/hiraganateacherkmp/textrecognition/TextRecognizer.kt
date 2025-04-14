package com.sechkarev.hiraganateacherkmp.textrecognition

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognition
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.google.mlkit.vision.digitalink.DigitalInkRecognizer
import com.google.mlkit.vision.digitalink.DigitalInkRecognizerOptions
import com.google.mlkit.vision.digitalink.Ink
import com.sechkarev.hiraganateacherkmp.model.Point
import kotlin.coroutines.suspendCoroutine

actual class TextRecognizer(
    private val remoteModelManager: RemoteModelManager,
) {
    private var inkBuilder = Ink.builder()
    private var strokeBuilder = Ink.Stroke.builder()

    private val digitalInkRecognitionModel: DigitalInkRecognitionModel
    private val recognizer: DigitalInkRecognizer

    init {
        val modelIdentifier =
            DigitalInkRecognitionModelIdentifier
                .fromLanguageTag("ja")
                ?: throw IllegalStateException("Could not find a recognition model identifier")
        digitalInkRecognitionModel =
            DigitalInkRecognitionModel
                .builder(modelIdentifier)
                .build()
        recognizer =
            DigitalInkRecognition
                .getClient(DigitalInkRecognizerOptions.builder(digitalInkRecognitionModel).build())
    }

    actual suspend fun init() =
        suspendCoroutine { continuation ->
            remoteModelManager
                .isModelDownloaded(digitalInkRecognitionModel)
                .addOnSuccessListener { isModelDownloaded ->
                    if (isModelDownloaded) {
                        continuation.resumeWith(Result.success(Unit))
                    } else {
                        remoteModelManager
                            .download(digitalInkRecognitionModel, DownloadConditions.Builder().build())
                            .addOnSuccessListener {
                                continuation.resumeWith(Result.success(Unit))
                            }.addOnFailureListener {
                                Log.e("TextRecognitionRepository", "remote model download error", it)
                                continuation.resumeWith(Result.failure(it))
                            }
                    }
                }.addOnFailureListener {
                    Log.e("TextRecognitionRepository", "could not determine if a model was downloaded", it)
                    continuation.resumeWith(Result.failure(it))
                }
        }

    actual suspend fun recognizeCurrentText(): String =
        suspendCoroutine { continuation ->
            recognizer
                .recognize(inkBuilder.build())
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it.candidates.first().text))
                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }

    actual fun cleanCurrentData() {
        inkBuilder = Ink.builder()
    }

    actual fun completeStroke() {
        inkBuilder.addStroke(strokeBuilder.build())
    }

    actual fun currentStrokeAmount() = inkBuilder.build().strokes.size

    actual fun startNewStroke() {
        strokeBuilder = Ink.Stroke.builder()
    }

    actual fun addNewPoint(point: Point) {
        strokeBuilder.addPoint(Ink.Point.create(point.x, point.y))
    }
}
