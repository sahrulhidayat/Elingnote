package com.sahi.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.sahi.core.model.entity.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query(value = "SELECT * FROM Notification")
    fun getAllNotifications(): Flow<List<Notification>>
    @Upsert
    suspend fun addOrUpdateNotification(notification: Notification)
    @Query(value = "DELETE FROM Notification WHERE id = :notificationId")
    suspend fun deleteNotification(notificationId: Int)
}