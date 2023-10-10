package com.sahi.core.database.di

import android.os.Build
import android.util.Log
import androidx.room.Room
import com.sahi.core.database.ElingNoteDatabase
import com.sahi.core.database.NotificationDatabase
import com.sahi.core.database.repository.ChecklistRepositoryImpl
import com.sahi.core.database.repository.NoteRepositoryImpl
import com.sahi.core.database.repository.NotificationRepositoryImpl
import com.sahi.usecase.repository.ChecklistRepository
import com.sahi.usecase.repository.NoteRepository
import com.sahi.usecase.repository.NotificationRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ElingNoteDatabase::class.java,
            ElingNoteDatabase.DATABASE_NAME
        ).build()
    }
    single {
        val database = get<ElingNoteDatabase>()
        database.noteDao
    }
    single {
        val database = get<ElingNoteDatabase>()
        database.checklistDao
    }

    single {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            var context = androidApplication().createDeviceProtectedStorageContext()
            if (!context.moveDatabaseFrom(
                    androidApplication(),
                    NotificationDatabase.DATABASE_NAME
                )
            ) {
                Log.w("Notification Database", "Failed to migrate database.")
                context = androidApplication()
            }
            Room.databaseBuilder(
                context,
                NotificationDatabase::class.java,
                NotificationDatabase.DATABASE_NAME
            ).build()
        } else {
            Room.databaseBuilder(
                androidApplication(),
                NotificationDatabase::class.java,
                NotificationDatabase.DATABASE_NAME
            ).build()
        }
    }
    single {
        val database = get<NotificationDatabase>()
        database.notificationDao
    }

    singleOf(::NoteRepositoryImpl) { bind<NoteRepository>() }
    singleOf(::ChecklistRepositoryImpl) { bind<ChecklistRepository>() }
    singleOf(::NotificationRepositoryImpl) { bind<NotificationRepository>() }
}