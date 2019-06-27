package com.sibedge.yokodzun.android.data

import ru.hnau.androidutils.preferences.PreferencesManager


object AuthManager: PreferencesManager("auth") {

    var adminAuthToken by newStringProperty("admin_auth_token")
    var raterCode by newStringProperty("rater_code")

}