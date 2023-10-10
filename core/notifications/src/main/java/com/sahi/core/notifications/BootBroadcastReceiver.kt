package com.sahi.core.notifications

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.os.UserManagerCompat
import com.sahi.usecase.NotificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class BootBroadcastReceiver : BroadcastReceiver(), KoinComponent {
    private val notificationUseCase: NotificationUseCase by inject()
    private val notificationScheduler: NotificationScheduler by inject()
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val bootCompleted = if (Build.VERSION.SDK_INT >= 24) {
            Intent.ACTION_LOCKED_BOOT_COMPLETED == action
        } else {
            Intent.ACTION_BOOT_COMPLETED == action
        }
        Log.i(
            TAG, "Received action: $action, user unlocked: " + UserManagerCompat
                .isUserUnlocked(context)
        )
        if (!bootCompleted) {
            return
        }

        goAsync {
            notificationUseCase.getAllNotifications()
                .collectLatest { notifications ->
                    if (notifications.isNotEmpty()) {
                        notifications.forEach {
                            notificationScheduler.schedule(it)
                        }
                    } else {
                        val receiver = ComponentName(context, BootBroadcastReceiver::class.java)
                        context.packageManager.setComponentEnabledSetting(
                            receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP
                        )
                    }
                }
        }
    }

    companion object {
        private const val TAG = "BootBroadcastReceiver"
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