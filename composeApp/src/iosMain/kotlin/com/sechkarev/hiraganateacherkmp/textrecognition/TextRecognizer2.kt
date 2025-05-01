package com.sechkarev.hiraganateacherkmp.textrecognition

import com.sechkarev.hiraganateacherkmp.model.Point

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
