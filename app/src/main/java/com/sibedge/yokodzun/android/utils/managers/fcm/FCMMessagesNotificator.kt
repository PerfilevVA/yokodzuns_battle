package com.sibedge.yokodzun.android.utils.managers.fcm

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.managers.notification.NotificationManager
import com.sibedge.yokodzun.common.data.notification.YNotification
import com.sibedge.yokodzun.common.data.notification.type.YNotificationBattleStarted
import com.sibedge.yokodzun.common.data.notification.type.YNotificationBattleStopped
import com.sibedge.yokodzun.common.data.notification.type.YNotificationCommon
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.jutils.me


object FCMMessagesNotificator {

    init {
        FCMMessagesReceiver.attach(this::onMessageReceived)
    }

    fun init() {}

    private fun onMessageReceived(message: YNotification) {
        when (message) {
            is YNotificationCommon -> onCommonMessageReceived(message)
            is YNotificationBattleStarted -> onBattleStartedMessageReceived(message)
            is YNotificationBattleStopped -> onBattleStoppedMessageReceived(message)
        }
    }

    private fun onCommonMessageReceived(message: YNotificationCommon) =
        NotificationManager.show(message.message.toGetter())

    private fun onBattleStartedMessageReceived(
        message: YNotificationBattleStarted
    ) = NotificationManager.show(
        StringGetter(
            R.string.notification_battle_started_text,
            message.battleName
        )
    )

    private fun onBattleStoppedMessageReceived(
        message: YNotificationBattleStopped
    ) = NotificationManager.show(
        StringGetter(
            R.string.notification_battle_stopped_text,
            message.battleName
        )
    )

}