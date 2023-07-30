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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ElingNoteDatabase::class.java,
            ElingNoteDatabase.DATABASE_NAME
        ).build()
    }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    single<ChecklistRepository> { ChecklistRepositoryImpl(get()) }

    viewModel {
        NotesViewModel(get())
        EditNoteViewModel(get(), get())
        ChecklistsViewModel(get())
        EditChecklistViewModel(get(), get())
        TrashViewModel(get(), get())
    }
}