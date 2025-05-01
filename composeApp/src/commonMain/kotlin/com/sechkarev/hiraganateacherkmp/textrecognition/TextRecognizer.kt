package com.sechkarev.hiraganateacherkmp.textrecognition

import com.sechkarev.hiraganateacherkmp.model.Point

// todo: expect interface
expect class TextRecognizer {
    suspend fun init()

    suspend fun recognizeCurrentText(): String

    fun cleanCurrentData()

    fun startNewStroke()

    fun completeStroke()

    fun currentStrokeAmount(): Int

    fun addNewPoint(point: Point)
}
