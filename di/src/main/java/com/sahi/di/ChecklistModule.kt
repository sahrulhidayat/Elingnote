package com.sahi.di

import com.sahi.feature.checklist.checklists.ChecklistsViewModel
import com.sahi.feature.checklist.edit_checklist.EditChecklistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val checklistModule = module {
    viewModelOf(::ChecklistsViewModel)
    viewModelOf(::EditChecklistViewModel)
}