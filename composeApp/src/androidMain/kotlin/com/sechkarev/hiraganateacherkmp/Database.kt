package com.sechkarev.hiraganateacherkmp

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.sechkarev.hiraganateacherkmp.data.database.ChallengeSolutionDatabase
import kotlinx.coroutines.Dispatchers

fun getDatabase(context: Context): ChallengeSolutionDatabase {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("challenges.db")
    return Room
        .databaseBuilder<ChallengeSolutionDatabase>(
            context = appContext,
            name = dbFile.absolutePath,
        ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
