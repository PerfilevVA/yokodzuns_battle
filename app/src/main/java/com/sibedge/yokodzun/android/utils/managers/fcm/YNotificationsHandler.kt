package com.sibedge.yokodzun.android.utils.managers.fcm

import android.app.PendingIntent
import android.content.Intent
import com.sibedge.yokodzun.android.AppActivity
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.managers.CrashliticsManager
import com.sibedge.yokodzun.android.utils.managers.notification.NotificationManager
import com.sibedge.yokodzun.common.data.notification.YNotification
import com.sibedge.yokodzun.common.data.notification.type.YNotificationCommon
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.utils.ContextConnector
import ru.hnau.androidutils.utils.generateId


object YNotificationsHandler {

    fun onNewYNotification(
        notification: YNotification
    ) = when (notification) {

        is YNotificationCommon -> {
            NotificationManager.show(
                text = notification.message.toGetter()
            )
        }

        //TODO

        else -> {
            CrashliticsManager.handle("Unknown notification class ${notification.javaClass.name}")
        }

    }

}