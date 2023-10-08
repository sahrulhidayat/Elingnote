package com.sahi.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sahi.core.database.dao.NotificationDao
import com.sahi.core.model.entity.Notification

@Database(
    entities = [
        Notification::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NotificationDatabase : RoomDatabase() {

    abstract val notificationDao: NotificationDao

    companion object {
        const val DATABASE_NAME = "notification_db"
    }
}