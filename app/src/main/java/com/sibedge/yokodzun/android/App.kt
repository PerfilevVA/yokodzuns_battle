package com.sibedge.yokodzun.android

import android.app.Application
import android.os.Handler
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType
import ru.hnau.androidutils.ui.utils.logD
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