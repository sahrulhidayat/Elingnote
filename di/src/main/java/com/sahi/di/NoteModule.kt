package com.sahi.di

import com.sahi.feature.note.edit_note.EditNoteViewModel
import com.sahi.feature.note.notes.NotesViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val noteModule = module{
    viewModelOf(::NotesViewModel)
    viewModelOf(::EditNoteViewModel)
}