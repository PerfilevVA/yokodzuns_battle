package com.sibedge.yokodzun.android

import android.app.Application
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType
import ru.hnau.androidutils.ui.utils.logD
import ru.hnau.androidutils.utils.ContextConnector
import com.sibedge.yokodzun.android.utils.managers.fcm.FCMManager


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        //TODO remove after fix
        logD(LayoutType.Stretched.toString())
        logD(LayoutType.DEFAULT.toString())

        ContextConnector.init(this)
        FCMManager.sendPushTokenIfNeed()
    }

}