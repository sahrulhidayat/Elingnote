package com.sahi.elingnote.di

import androidx.room.Room
import com.sahi.elingnote.data.repository.ChecklistRepository
import com.sahi.elingnote.data.repository.ChecklistRepositoryImpl
import com.sahi.elingnote.data.repository.NoteRepository
import com.sahi.elingnote.data.repository.NoteRepositoryImpl
import com.sahi.elingnote.data.source.ElingNoteDatabase
import com.sahi.elingnote.ui.checklist_feature.checklists.ChecklistsViewModel
import com.sahi.elingnote.ui.checklist_feature.edit_checklist.EditChecklistViewModel
import com.sahi.elingnote.ui.note_feature.edit_note.EditNoteViewModel
import com.sahi.elingnote.ui.note_feature.notes.NotesViewModel
import com.sahi.elingnote.ui.trash_feature.TrashViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
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
    viewModelOf(::NotesViewModel)
    viewModelOf(::EditNoteViewModel)
    viewModelOf(::ChecklistsViewModel)
    viewModelOf(::EditChecklistViewModel)
    viewModelOf(::TrashViewModel)
}