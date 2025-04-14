package com.sechkarev.hiraganateacherkmp

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.sechkarev.hiraganateacherkmp.database.ChallengeSolutionDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getDatabase(): ChallengeSolutionDatabase {
    val dbFile = "${fileDirectory()}/challenges.db"
    return Room
        .databaseBuilder<ChallengeSolutionDatabase>(
            name = dbFile,
        ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@OptIn(ExperimentalForeignApi::class)
private fun fileDirectory(): String {
    val documentDirectory =
        NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
    return requireNotNull(documentDirectory).path!!
}
