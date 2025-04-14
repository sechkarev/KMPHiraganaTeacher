package com.sechkarev.hiraganateacherkmp.domain

import com.sechkarev.hiraganateacherkmp.model.GameProgress
import kotlinx.coroutines.flow.MutableStateFlow

class GameRepository {
    val gameProgress = MutableStateFlow<GameProgress>(GameProgress())
}
