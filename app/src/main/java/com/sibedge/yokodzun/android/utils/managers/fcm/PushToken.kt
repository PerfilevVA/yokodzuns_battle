package com.sibedge.yokodzun.android.utils.managers.fcm

import com.sibedge.yokodzun.android.utils.managers.SettingsManager


object PushToken {

    fun createKey(pushToken: String) =
        SettingsManager.host.let { "${it.length}$it$pushToken" }

}