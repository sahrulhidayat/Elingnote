package com.sahi.core.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.sahi.usecase.NotificationUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val REMINDER_CHANNEL = "reminder_channel"

class NotificationReceiver : BroadcastReceiver(), KoinComponent {
    private val notificationUseCase: NotificationUseCase by inject()
    override fun onReceive(context: Context, intent: Intent) {
        showNotification(intent, context)
    }

    private fun showNotification(intent: Intent, context: Context) {
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
        goAsync {
            notificationUseCase.deleteReminder(id)
        }
    }
}