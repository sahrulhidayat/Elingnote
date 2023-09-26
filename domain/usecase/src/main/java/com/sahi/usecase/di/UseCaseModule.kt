package com.sahi.usecase.di

import com.sahi.usecase.ChecklistUseCase
import com.sahi.usecase.ChecklistUseCaseImpl
import com.sahi.usecase.NoteUseCase
import com.sahi.usecase.NoteUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    single { NoteUseCaseImpl(get()) }
    single { ChecklistUseCaseImpl(get()) }

    singleOf(::NoteUseCaseImpl) { bind<NoteUseCase>() }
    singleOf(::ChecklistUseCaseImpl) { bind<ChecklistUseCase>() }
}