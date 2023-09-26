package com.sahi.core.database.di

import androidx.room.Room
import com.sahi.core.database.ElingNoteDatabase
import com.sahi.core.database.repository.ChecklistRepositoryImpl
import com.sahi.core.database.repository.NoteRepositoryImpl
import com.sahi.usecase.repository.ChecklistRepository
import com.sahi.usecase.repository.NoteRepository
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

    singleOf(::NoteRepositoryImpl) { bind<NoteRepository>() }
    singleOf(::ChecklistRepositoryImpl) { bind<ChecklistRepository>() }
}