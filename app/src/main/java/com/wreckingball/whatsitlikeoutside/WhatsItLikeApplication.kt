package com.wreckingball.whatsitlikeoutside

import android.app.Application
import com.wreckingball.whatsitlikeoutside.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WhatsItLikeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WhatsItLikeApplication)
            modules(appModule)
        }
    }

}