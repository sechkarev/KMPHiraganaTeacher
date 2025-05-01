package com.sechkarev.hiraganateacherkmp.textrecognition

expect interface TextRecognizer2 {
    fun initialize(
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    )
}
