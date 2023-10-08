package com.sahi.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.sahi.core.model.entity.Notification

class NotificationSchedulerImpl(
    private val context: Context,
): NotificationScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(notification: Notification) {
        val receiver = ComponentName(context, NotificationReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("TITLE_EXTRA", notification.title)
            putExtra("CONTENT_EXTRA", notification.content)
            putExtra("ID_EXTRA", notification.id)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notification.time,
                PendingIntent.getBroadcast(
                    context,
                    notification.id ?: 0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                notification.time,
                PendingIntent.getBroadcast(
                    context,
                    notification.id ?: 0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    override fun cancel(requestCode: Int) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(context, NotificationReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}

interface NotificationScheduler {
    fun schedule(notification: Notification)
    fun cancel(requestCode: Int)
}