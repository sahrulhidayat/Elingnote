package com.sahi.elingnote.di

import androidx.room.Room
import com.sahi.core.database.repository.ChecklistRepository
import com.sahi.core.database.repository.ChecklistRepositoryImpl
import com.sahi.core.database.repository.NoteRepository
import com.sahi.core.database.repository.NoteRepositoryImpl
import com.sahi.core.database.ElingNoteDatabase
import com.sahi.feature.checklist.checklists.ChecklistsViewModel
import com.sahi.feature.checklist.edit_checklist.EditChecklistViewModel
import com.sahi.feature.note.edit_note.EditNoteViewModel
import com.sahi.feature.note.notes.NotesViewModel
import com.sahi.feature.trash.TrashViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            com.sahi.core.database.ElingNoteDatabase::class.java,
            com.sahi.core.database.ElingNoteDatabase.DATABASE_NAME
        ).build()
    }
    single {
        val database = get<com.sahi.core.database.ElingNoteDatabase>()
        database.noteDao
    }
    single {
        val database = get<com.sahi.core.database.ElingNoteDatabase>()
        database.checklistDao
    }
    singleOf(::NoteRepositoryImpl) { bind<com.sahi.core.database.repository.NoteRepository>() }
    singleOf(::ChecklistRepositoryImpl) { bind<com.sahi.core.database.repository.ChecklistRepository>() }
    viewModelOf(::NotesViewModel)
    viewModelOf(::EditNoteViewModel)
    viewModelOf(::ChecklistsViewModel)
    viewModelOf(::EditChecklistViewModel)
    viewModelOf(::TrashViewModel)
}