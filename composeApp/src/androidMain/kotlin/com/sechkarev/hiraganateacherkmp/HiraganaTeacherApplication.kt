package com.sechkarev.hiraganateacherkmp

import android.app.Application
import com.sechkarev.hiraganateacherkmp.di.appModule
import com.sechkarev.hiraganateacherkmp.di.initKoin
import com.sechkarev.hiraganateacherkmp.di.platformModule
import org.koin.android.ext.koin.androidContext

class HiraganaTeacherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@HiraganaTeacherApplication)
            modules(appModule, platformModule)
        }
    }
}
