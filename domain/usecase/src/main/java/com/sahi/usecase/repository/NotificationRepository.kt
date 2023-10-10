package com.sahi.usecase.repository

import com.sahi.core.model.entity.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getAllNotifications(): Flow<List<Notification>>
    suspend fun addOrUpdateNotification(notification: Notification)
    suspend fun deleteNotification(id: Int)
}