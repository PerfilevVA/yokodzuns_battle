package com.sibedge.yokodzun.android.utils.managers

import com.crashlytics.android.Crashlytics
import com.sibedge.yokodzun.common.exception.ApiException


object CrashliticsManager {

    fun handle(message: String) =
        handle(ApiException.raw(message))

    fun handle(th: Throwable) {
        if (th is ApiException) {
            Crashlytics.log("Error: ${th.toString()}")
        } else {
            Crashlytics.logException(th)
        }
    }

}