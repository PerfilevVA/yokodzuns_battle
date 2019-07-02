package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.utils.managers.fcm.FCMMessagesReceiver
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.notification.type.YNotificationBattleStarted
import com.sibedge.yokodzun.common.data.notification.type.YNotificationBattleStopped


object RaterBattleDataManager : YDataManager<Battle>() {

    init {
        FCMMessagesReceiver.attach { notification ->
            if (
                notification is YNotificationBattleStarted ||
                notification is YNotificationBattleStopped
            ) {
                invalidate()
            }
        }
    }

    override suspend fun getValue() =
        API.battleForRater().await()

}