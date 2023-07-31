package com.sahi.elingnote

import android.app.Application
import com.sahi.elingnote.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ElingNoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ElingNoteApplication)
            modules(appModule)
        }
    }
}