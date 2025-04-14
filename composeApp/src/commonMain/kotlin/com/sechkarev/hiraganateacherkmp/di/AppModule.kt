package com.sechkarev.hiraganateacherkmp.di

import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.ui.characters.CharacterListViewModel
import com.sechkarev.hiraganateacherkmp.ui.dictionary.DictionaryViewModel
import com.sechkarev.hiraganateacherkmp.ui.game.GameViewModel
import com.sechkarev.hiraganateacherkmp.ui.mainmenu.MainMenuViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val appModule =
    module {
        factoryOf(::GameRepository)
        viewModelOf(::CharacterListViewModel)
        viewModelOf(::DictionaryViewModel)
        viewModelOf(::MainMenuViewModel)
        viewModelOf(::GameViewModel)
    }
