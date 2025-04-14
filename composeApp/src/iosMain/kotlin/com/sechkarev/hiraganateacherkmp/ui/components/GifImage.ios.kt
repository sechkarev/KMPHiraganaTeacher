package com.sechkarev.hiraganateacherkmp.ui.components

import io.kamel.core.utils.File
import kmphiraganateacher.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
actual suspend fun getResourceFile(fileResourcePath: String): File = File(Res.getUri(fileResourcePath).substringAfter("file://"))
