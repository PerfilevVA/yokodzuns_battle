package com.sibedge.yokodzun.android.utils.managers

import ru.hnau.androidutils.preferences.PreferencesManager


object SettingsManager : PreferencesManager("SETTINGS") {

    private const val DEFAULT_HOST = "http://hnau.org:8080"

    val hostProperty = newStringProperty("HOST", DEFAULT_HOST)
    var host: String by hostProperty

}