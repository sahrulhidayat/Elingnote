package com.sahi.usecase.repository

import com.sahi.core.model.entity.Notification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getAllNotifications(): Flow<List<Notification>>
    fun addOrUpdateNotification(notification: Notification)
    fun deleteNotification(notification: Notification)
}