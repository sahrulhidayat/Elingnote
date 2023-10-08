package com.sahi.usecase

import com.sahi.core.model.entity.Notification
import com.sahi.usecase.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class NotificationUseCaseImpl(
    private val notificationRepository: NotificationRepository
) : NotificationUseCase {

    override suspend fun getAllNotifications(): Flow<List<Notification>> {
        return notificationRepository.getAllNotifications()
    }

    override fun addReminder(notification: Notification): Long {
        return notificationRepository.addOrUpdateNotification(notification)
    }

    override fun deleteReminder(notification: Notification) {
        return notificationRepository.deleteNotification(notification)
    }

}

interface NotificationUseCase {
    suspend fun getAllNotifications(): Flow<List<Notification>>
    fun addReminder(notification: Notification): Long
    fun deleteReminder(notification: Notification)
}