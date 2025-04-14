package com.sechkarev.hiraganateacherkmp.di

import com.sechkarev.hiraganateacherkmp.domain.GameRepository
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

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, databaseModule)
    }
}

expect val databaseModule: Module

val appModule =
    module {
        factoryOf(::GameRepository)
        viewModelOf(::CharacterListViewModel)
        viewModelOf(::DictionaryViewModel)
        viewModelOf(::MainMenuViewModel)
        viewModelOf(::GameViewModel)
    }
