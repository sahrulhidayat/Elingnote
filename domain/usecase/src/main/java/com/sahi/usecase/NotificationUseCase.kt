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

}

interface NotificationUseCase {
    suspend fun getAllNotifications(): Flow<List<Notification>>
}