package com.sechkarev.hiraganateacherkmp.di

import com.sechkarev.hiraganateacherkmp.database.ChallengeSolutionDatabase
import com.sechkarev.hiraganateacherkmp.getDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val databaseModule: Module =
    module {
        single { getDatabase() }
        factory { get<ChallengeSolutionDatabase>().challengeSolutionDao() }
    }
