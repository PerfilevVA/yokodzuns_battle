package com.sibedge.yokodzun.common.data.notification.type

import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.notification.YNotification


class YNotificationBattleStopped(
        val battleId: String,
        val battleName: String
) : YNotification() {

    override fun generateDescription() =
            "battleId=$battleId&battleName=$battleName"

}