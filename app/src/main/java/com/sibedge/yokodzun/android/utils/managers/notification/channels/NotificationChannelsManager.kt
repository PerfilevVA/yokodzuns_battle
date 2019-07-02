package com.sibedge.yokodzun.android.utils.managers.notification.channels

import android.app.NotificationManager
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import ru.hnau.androidutils.utils.ContextConnector

object NotificationChannelsManager {

    private var initialized = false

    val IMPORTANCE_DEFAULT =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) 0 else NotificationManager.IMPORTANCE_DEFAULT

    fun initializeIfNeed() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        synchronized(this) {
            if (initialized) {
                return@synchronized
            }
            initialize()
            initialized = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initialize() {
        val notificationManager =
                ContextConnector.context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        NotificationChannelType.values().forEach { type ->
            notificationManager.createNotificationChannel(type.createChannel())
        }
    }

}