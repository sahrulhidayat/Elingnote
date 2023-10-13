package com.sahi.usecase.repository

import com.sahi.core.model.entity.Notification

interface NotificationRepository {
    fun getAllNotifications(): List<Notification>
    suspend fun addOrUpdateNotification(notification: Notification)
    suspend fun deleteNotification(id: Int)
}