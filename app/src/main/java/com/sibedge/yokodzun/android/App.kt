package com.sibedge.yokodzun.android

import android.app.Application
import ru.hnau.androidutils.utils.ContextConnector
import com.sibedge.yokodzun.android.utils.managers.fcm.FCMManager
import com.sibedge.yokodzun.android.utils.managers.fcm.FCMMessagesNotificator


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextConnector.init(this)
        FCMManager.sendPushTokenIfNeed()
        FCMMessagesNotificator.init()
    }

}