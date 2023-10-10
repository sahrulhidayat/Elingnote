package com.sahi.core.database.repository

import com.sahi.core.database.dao.NotificationDao
import com.sahi.core.model.entity.Notification
import com.sahi.usecase.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl(
    private val dao: NotificationDao
) : NotificationRepository {
    override fun getAllNotifications(): Flow<List<Notification>> {
        return dao.getAllNotifications()
    }

    override suspend fun addOrUpdateNotification(notification: Notification) {
        return dao.addOrUpdateNotification(notification)
    }

    override suspend fun deleteNotification(id: Int) {
        return dao.deleteNotification(id)
    }
}