package com.sahi.core.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.sahi.usecase.NotificationUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val REMINDER_CHANNEL = "reminder_channel"

class NotificationReceiver : BroadcastReceiver(), KoinComponent {

    private val notificationUseCase: NotificationUseCase by inject()
    private val notificationScheduler: NotificationScheduler by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED"
            || intent.action == "android.intent.action.LOCKED_BOOT_COMPLETED"
        ) {
            val notifications = notificationUseCase.getAllScheduledNotifications()
            Log.d("Notifications", "notifications: $notifications")

            if (notifications.isNotEmpty()) {
                notifications.forEach {
                    notificationScheduler.schedule(
                        it.requestCode,
                        it.title,
                        it.content,
                        it.time
                    )
                }
            } else {
                val receiver = ComponentName(context, NotificationReceiver::class.java)
                context.packageManager.setComponentEnabledSetting(
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
        } else {
            val title = intent.getStringExtra("TITLE_EXTRA")
            val content = intent.getStringExtra("CONTENT_EXTRA")
            val id = intent.getIntExtra("ID_EXTRA", 1)

            val notification = NotificationCompat.Builder(context, REMINDER_CHANNEL)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_quill)
                .setStyle(NotificationCompat.BigTextStyle())
                .build()

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(id, notification)
        }
    }
}