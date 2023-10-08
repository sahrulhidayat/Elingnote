package com.sahi.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.sahi.core.model.entity.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query(value = "SELECT * FROM Notification")
    fun getAllNotifications(): Flow<List<Notification>>
    @Upsert
    fun addOrUpdateNotification(notification: Notification)
    @Delete
    fun deleteNotification(notification: Notification)
}