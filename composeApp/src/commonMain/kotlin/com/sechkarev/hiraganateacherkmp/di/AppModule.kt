package com.sechkarev.hiraganateacherkmp.di

import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.textrecognition.TextRecognizer2
import com.sechkarev.hiraganateacherkmp.ui.characters.CharacterListViewModel
import com.sechkarev.hiraganateacherkmp.ui.dictionary.DictionaryViewModel
import com.sechkarev.hiraganateacherkmp.ui.game.GameViewModel
import com.sechkarev.hiraganateacherkmp.ui.mainmenu.MainMenuViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    textRecognizer2: TextRecognizer2,
    config: KoinAppDeclaration? = null,
) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            platformModule,
            module {
                single { textRecognizer2 }
            },
        )
    }
}

expect val platformModule: Module

val appModule =
    module {
        factoryOf(::GameRepository)
        viewModelOf(::CharacterListViewModel)
        viewModelOf(::DictionaryViewModel)
        viewModelOf(::MainMenuViewModel)
        viewModelOf(::GameViewModel)
    }
