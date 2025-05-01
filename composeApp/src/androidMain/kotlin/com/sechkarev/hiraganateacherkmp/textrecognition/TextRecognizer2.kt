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

actual interface TextRecognizer2 {
    actual fun initialize(
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    )

    actual suspend fun recognizeCurrentText(): String

    actual fun cleanCurrentData()

    actual fun startNewStroke()

    actual fun completeStroke()

    actual fun currentStrokeAmount(): Int

    actual fun addNewPoint(point: Point)
}

class TextRecognizer2Impl : TextRecognizer2 {
    private var inkBuilder = Ink.builder()
    private var strokeBuilder = Ink.Stroke.builder()

    private val remoteModelManager = RemoteModelManager.getInstance()
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

    override fun initialize(
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    ) {
        remoteModelManager
            .isModelDownloaded(digitalInkRecognitionModel)
            .addOnSuccessListener { isModelDownloaded ->
                if (isModelDownloaded) {
                    onSuccess()
                } else {
                    remoteModelManager
                        .download(digitalInkRecognitionModel, DownloadConditions.Builder().build())
                        .addOnSuccessListener {
                            onSuccess()
                        }.addOnFailureListener {
                            Log.e("TextRecognitionRepository", "remote model download error", it)
                            onFailure()
                        }
                }
            }.addOnFailureListener {
                Log.e("TextRecognitionRepository", "could not determine if a model was downloaded", it)
                onFailure()
            }
    }

    override suspend fun recognizeCurrentText(): String =
        suspendCoroutine { continuation ->
            recognizer
                .recognize(inkBuilder.build())
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(it.candidates.first().text))
                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }

    override fun cleanCurrentData() {
        inkBuilder = Ink.builder()
    }

    override fun completeStroke() {
        inkBuilder.addStroke(strokeBuilder.build())
    }

    override fun currentStrokeAmount() = inkBuilder.build().strokes.size

    override fun startNewStroke() {
        strokeBuilder = Ink.Stroke.builder()
    }

    override fun addNewPoint(point: Point) {
        strokeBuilder.addPoint(Ink.Point.create(point.x, point.y))
    }
}
