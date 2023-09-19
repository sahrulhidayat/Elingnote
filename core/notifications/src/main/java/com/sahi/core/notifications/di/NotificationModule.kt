package com.sahi.core.notifications.di

import com.sahi.core.notifications.NotificationScheduler
import com.sahi.core.notifications.NotificationSchedulerImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val notificationModule = module {
    singleOf(::NotificationSchedulerImpl) { bind<NotificationScheduler>() }
}