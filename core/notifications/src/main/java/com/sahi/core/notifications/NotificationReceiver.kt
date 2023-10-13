package com.sahi.core.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.UserManagerCompat
import com.sahi.usecase.NotificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

const val REMINDER_CHANNEL = "reminder_channel"

class NotificationReceiver : BroadcastReceiver(), KoinComponent {
    private val notificationUseCase: NotificationUseCase by inject()
    private val notificationScheduler: NotificationScheduler by inject()
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.i(
            TAG, "Received action: $action, user unlocked: " + UserManagerCompat
                .isUserUnlocked(context)
        )

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val notifications = notificationUseCase.getAllNotifications()
            Log.i(TAG, "Notifications: $notifications")

            notifications.forEach {
                notificationScheduler.schedule(it)
            }

            if (notifications.isEmpty()) {
                val receiver = ComponentName(context, NotificationReceiver::class.java)
                context.packageManager.setComponentEnabledSetting(
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
        } else {
            showNotification(intent, context)
        }
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

    companion object {
        private const val TAG = "NotificationReceiver"
    }
}

fun BroadcastReceiver.goAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult = goAsync()
    CoroutineScope(SupervisorJob()).launch(context) {
        try {
            block()
        } finally {
            pendingResult.finish()
        }
    }
}