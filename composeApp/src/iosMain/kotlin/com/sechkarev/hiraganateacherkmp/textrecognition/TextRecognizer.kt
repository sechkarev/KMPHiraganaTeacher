package com.sechkarev.hiraganateacherkmp.textrecognition

import com.sechkarev.hiraganateacherkmp.model.Point

// todo: add text recognition to iOS
actual class TextRecognizer {
    actual suspend fun init() {
    }

    actual suspend fun recognizeCurrentText(): String = ""

    actual fun cleanCurrentData() {
    }

    actual fun completeStroke() {
    }

    actual fun currentStrokeAmount() = 0

    actual fun startNewStroke() {
    }

    actual fun addNewPoint(point: Point) {
    }
}
