package com.sahi.elingnote

import android.app.Application
import com.sahi.core.database.di.databaseModule
import com.sahi.feature.checklist.di.checklistModule
import com.sahi.feature.note.di.noteModule
import com.sahi.feature.trash.di.trashModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                listOf(
                    databaseModule,
                    noteModule,
                    checklistModule,
                    trashModule
                )
            )
        }
    }
}