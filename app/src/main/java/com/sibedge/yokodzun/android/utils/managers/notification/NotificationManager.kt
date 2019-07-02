package com.sibedge.yokodzun.android.utils.managers.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sibedge.yokodzun.android.AppActivity
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.managers.notification.channels.NotificationChannelType
import com.sibedge.yokodzun.android.utils.managers.notification.channels.NotificationChannelsManager
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.utils.ContextConnector
import ru.hnau.androidutils.utils.generateId


object NotificationManager {

    private const val DEFAULT_ID = 0

    private val notificationManager =
        NotificationManagerCompat.from(ContextConnector.context)

    private val DEFAULT_ON_CLICK: PendingIntent
        get() = PendingIntent.getActivity(
            ContextConnector.context,
            generateId(),
            Intent(ContextConnector.context, AppActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    fun show(
        text: StringGetter,
        id: Int = DEFAULT_ID,
        channelType: NotificationChannelType = NotificationChannelType.DEFAULT,
        title: StringGetter = StringGetter(R.string.app_name),
        iconResId: Int = R.drawable.ic_notification,
        onClick: PendingIntent = DEFAULT_ON_CLICK,
        configurator: NotificationCompat.Builder.() -> Unit = {}
    ) {
        val context = ContextConnector.context
        val notification = NotificationCompat.Builder(
            context, channelType.id
        ).apply {
            setContentTitle(title.get(context))
            setContentText(text.get(context))
            setSmallIcon(iconResId)
            setContentIntent(onClick)
            setAutoCancel(true)
            configurator.invoke(this)
        }.build()
        show(id, notification)
    }

    private fun show(
        id: Int,
        notification: Notification
    ) {
        NotificationChannelsManager.initializeIfNeed()
        notificationManager.notify(id, notification)
    }

}