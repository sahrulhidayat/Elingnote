package com.sahi.core.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder

class NotificationIntentServiceImpl(
    private val context: Context
) : NotificationIntentService {
    override fun get(): PendingIntent? {
        val notificationIntent = Intent(
            context,
            Class.forName("com.sahi.elingnote.MainActivity")
        )
        val notificationPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(notificationIntent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        return notificationPendingIntent
    }
}

interface NotificationIntentService {
    fun get(): PendingIntent?
}