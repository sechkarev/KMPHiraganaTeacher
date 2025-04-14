package com.sechkarev.hiraganateacherkmp.model

import kotlinx.serialization.Serializable

@Serializable
data class Stroke(
    val path: List<Point>,
)
