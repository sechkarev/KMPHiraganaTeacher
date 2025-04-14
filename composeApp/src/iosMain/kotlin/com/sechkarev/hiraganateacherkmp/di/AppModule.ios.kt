package com.sechkarev.hiraganateacherkmp.di

import com.sechkarev.hiraganateacherkmp.database.ChallengeSolutionDatabase
import com.sechkarev.hiraganateacherkmp.getDatabase
import com.sechkarev.hiraganateacherkmp.textrecognition.TextRecognizer
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module =
    module {
        single { getDatabase() }
        factory { get<ChallengeSolutionDatabase>().challengeSolutionDao() }
        single { TextRecognizer() }
    }
