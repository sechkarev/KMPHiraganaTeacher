package com.sechkarev.hiraganateacherkmp

import android.app.Application
import com.sechkarev.hiraganateacherkmp.di.initKoin
import com.sechkarev.hiraganateacherkmp.textrecognition.TextRecognizer2Impl
import org.koin.android.ext.koin.androidContext

class HiraganaTeacherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            textRecognizer2 = TextRecognizer2Impl(),
        ) {
            androidContext(this@HiraganaTeacherApplication)
        }
    }
}
