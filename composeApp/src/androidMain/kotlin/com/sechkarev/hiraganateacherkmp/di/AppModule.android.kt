package com.sechkarev.hiraganateacherkmp.di

import com.google.mlkit.common.model.RemoteModelManager
import com.sechkarev.hiraganateacherkmp.database.ChallengeSolutionDatabase
import com.sechkarev.hiraganateacherkmp.getDatabase
import com.sechkarev.hiraganateacherkmp.textrecognition.TextRecognizer
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module =
    module {
        single<ChallengeSolutionDatabase> { getDatabase(context = get()) }
        factory { get<ChallengeSolutionDatabase>().challengeSolutionDao() }
        single {
            TextRecognizer(RemoteModelManager.getInstance())
        }
    }
