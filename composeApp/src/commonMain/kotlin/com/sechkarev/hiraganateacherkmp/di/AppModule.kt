package com.sechkarev.hiraganateacherkmp.di

import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.ui.characters.CharacterListViewModel
import com.sechkarev.hiraganateacherkmp.ui.dictionary.DictionaryViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule =
    module {
        factory {
            GameRepository()
        }
        viewModelOf(::CharacterListViewModel)
        viewModelOf(::DictionaryViewModel)
    }
