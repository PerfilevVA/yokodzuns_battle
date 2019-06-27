package com.sibedge.yokodzun.android.data

import ru.hnau.androidutils.preferences.PreferencesManager
import ru.hnau.jutils.takeIfNotEmpty
import java.util.*


object AppInstanceManager : PreferencesManager("APP_INSTANCE") {

    private var uuidInner: String by newStringProperty("UUID")

    val uuid: String
        get() {
            var result = uuidInner.takeIfNotEmpty()
            if (result == null) {
                result = UUID.randomUUID().toString()
                uuidInner = result
            }
            return result
        }


}