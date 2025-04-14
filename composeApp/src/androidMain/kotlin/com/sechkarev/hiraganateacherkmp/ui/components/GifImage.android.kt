package com.sechkarev.hiraganateacherkmp.ui.components

import io.kamel.core.utils.File
import kmphiraganateacher.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import java.io.FileOutputStream

@OptIn(ExperimentalResourceApi::class)
actual suspend fun getResourceFile(fileResourcePath: String): File {
    val file = java.io.File.createTempFile("temp", ".${fileResourcePath.substringAfterLast(".")}")
    FileOutputStream(file).use { os ->
        val buffer = Res.readBytes(fileResourcePath)
        os.write(buffer, 0, buffer.size)
    }
    return file
}
