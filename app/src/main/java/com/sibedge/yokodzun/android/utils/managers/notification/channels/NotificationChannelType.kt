package com.sibedge.yokodzun.android.utils.managers.notification.channels

import android.app.NotificationChannel
import android.os.Build
import android.support.annotation.RequiresApi
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.utils.ContextConnector
import com.sibedge.yokodzun.android.R


enum class NotificationChannelType(
        val id: String,
        val title: StringGetter,
        val description: StringGetter,
        val importance: Int
) {

    DEFAULT(
            id = "DEFAULT",
            title = StringGetter(R.string.notification_channel_title_default),
            description = StringGetter(R.string.notification_channel_description_default),
            importance = NotificationChannelsManager.IMPORTANCE_DEFAULT
    );

    @RequiresApi(Build.VERSION_CODES.O)
    fun createChannel(): NotificationChannel {
        val context = ContextConnector.context
        return NotificationChannel(id, title.get(context), importance).apply {
            description = this@NotificationChannelType.description.get(context)
        }
    }

}