package com.sechkarev.hiraganateacherkmp

import android.app.Application
import com.sechkarev.hiraganateacherkmp.di.appModule
import org.koin.core.context.startKoin

class HiraganaTeacherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
//            androidContext(this@HiraganaTeacherApplication)
            modules(appModule)
        }
    }
}
