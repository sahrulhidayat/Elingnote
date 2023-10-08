package com.sahi.core.database.repository

import com.sahi.core.database.dao.NotificationDao
import com.sahi.core.model.entity.Notification
import com.sahi.usecase.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl(
    private val dao: NotificationDao
) : NotificationRepository {
    override suspend fun getAllNotifications(): Flow<List<Notification>> {
        return dao.getAllNotifications()
    }

    override fun addOrUpdateNotification(notification: Notification): Long {
        return dao.addOrUpdateNotification(notification)
    }

    override fun deleteNotification(notification: Notification) {
        return dao.deleteNotification(notification)
    }
}