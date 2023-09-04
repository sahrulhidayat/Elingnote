package com.sahi.core.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "channel_1"
class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(intent.getStringExtra("TITLE_EXTRA"))
            .setContentText(intent.getStringExtra("CONTENT_EXTRA"))
            .setSmallIcon(R.drawable.ic_quill)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)

    }
}