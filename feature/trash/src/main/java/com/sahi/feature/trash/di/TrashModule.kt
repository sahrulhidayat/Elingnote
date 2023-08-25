package com.sahi.feature.trash.di

import com.sahi.feature.trash.TrashViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val trashModule = module {
    viewModelOf(::TrashViewModel)
}