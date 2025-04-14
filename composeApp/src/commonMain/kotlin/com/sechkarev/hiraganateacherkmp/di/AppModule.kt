package com.sechkarev.hiraganateacherkmp.di

import com.sechkarev.hiraganateacherkmp.domain.GameRepository
import com.sechkarev.hiraganateacherkmp.ui.characters.CharacterListViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        factory {
            GameRepository()
        }

        viewModel {
            CharacterListViewModel(get())
        }
    }
