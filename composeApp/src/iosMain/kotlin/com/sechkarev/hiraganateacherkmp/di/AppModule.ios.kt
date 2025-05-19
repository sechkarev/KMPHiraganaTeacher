package com.sechkarev.hiraganateacherkmp.di

import com.sechkarev.hiraganateacherkmp.data.database.ChallengeSolutionDatabase
import com.sechkarev.hiraganateacherkmp.getDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module =
    module {
        single { getDatabase() }
        factory { get<ChallengeSolutionDatabase>().challengeSolutionDao() }
        factory { get<ChallengeSolutionDatabase>().userEventDao() }
    }
