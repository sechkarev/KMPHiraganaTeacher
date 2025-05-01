package com.sechkarev.hiraganateacherkmp.di

import com.sechkarev.hiraganateacherkmp.database.ChallengeSolutionDatabase
import com.sechkarev.hiraganateacherkmp.getDatabase
import com.sechkarev.hiraganateacherkmp.tts.TextToSpeechEngine
import com.sechkarev.hiraganateacherkmp.tts.TextToSpeechEngineImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module =
    module {
        single<ChallengeSolutionDatabase> { getDatabase(context = get()) }
        single<TextToSpeechEngine> { TextToSpeechEngineImpl(context = get()) }
        factory { get<ChallengeSolutionDatabase>().challengeSolutionDao() }
    }
