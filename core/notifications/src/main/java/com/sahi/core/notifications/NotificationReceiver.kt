package com.sahi.core.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val REMINDER_CHANNEL = "reminder_channel"
class NotificationReceiver: BroadcastReceiver(), KoinComponent {

    val notificationScheduler: NotificationScheduler by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // Set the alarm here.
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

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(id, notification)
        }
    }
}