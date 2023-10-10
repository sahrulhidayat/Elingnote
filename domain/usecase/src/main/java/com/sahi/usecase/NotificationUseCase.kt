package com.sahi.usecase

import com.sahi.core.model.entity.Notification
import com.sahi.usecase.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class NotificationUseCaseImpl(
    private val notificationRepository: NotificationRepository
) : NotificationUseCase {

    override fun getAllNotifications(): Flow<List<Notification>> {
        return notificationRepository.getAllNotifications()
    }

    override suspend fun addReminder(notification: Notification) {
        return notificationRepository.addOrUpdateNotification(notification)
    }

    override suspend fun deleteReminder(id: Int) {
        return notificationRepository.deleteNotification(id)
    }

}

interface NotificationUseCase {
    fun getAllNotifications(): Flow<List<Notification>>
    suspend fun addReminder(notification: Notification)
    suspend fun deleteReminder(id: Int)
}