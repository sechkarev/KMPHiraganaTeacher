package com.sechkarev.hiraganateacherkmp.textrecognition

import com.sechkarev.hiraganateacherkmp.model.Point

expect interface TextRecognizer2 {
    fun initialize(
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    )

    suspend fun recognizeCurrentText(): String

    fun cleanCurrentData()

    fun startNewStroke()

    fun completeStroke()

    fun currentStrokeAmount(): Int

    fun addNewPoint(point: Point)
}
