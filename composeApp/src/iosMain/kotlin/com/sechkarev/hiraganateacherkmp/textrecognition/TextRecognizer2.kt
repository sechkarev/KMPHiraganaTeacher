package com.sechkarev.hiraganateacherkmp.textrecognition

actual interface TextRecognizer2 {
    actual fun initialize(
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
    )
}
