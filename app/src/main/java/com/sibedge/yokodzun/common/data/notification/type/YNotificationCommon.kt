package com.sibedge.yokodzun.common.data.notification.type

import com.sibedge.yokodzun.common.data.notification.YNotification


class YNotificationCommon(
        val message: String
) : YNotification() {

    override fun generateDescription() =
            "message=$message"

}