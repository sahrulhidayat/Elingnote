package com.sahi.di

import com.sahi.usecase.ChecklistUseCase
import com.sahi.usecase.ChecklistUseCaseImpl
import com.sahi.usecase.NoteUseCase
import com.sahi.usecase.NoteUseCaseImpl
import com.sahi.usecase.NotificationUseCase
import com.sahi.usecase.NotificationUseCaseImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    single { NoteUseCaseImpl(get()) }
    single { ChecklistUseCaseImpl(get()) }
    single { NotificationUseCaseImpl(get()) }

    singleOf(::NoteUseCaseImpl) { bind<NoteUseCase>() }
    singleOf(::ChecklistUseCaseImpl) { bind<ChecklistUseCase>() }
    singleOf(::NotificationUseCaseImpl) { bind<NotificationUseCase>() }
}