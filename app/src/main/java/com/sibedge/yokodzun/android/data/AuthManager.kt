package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.common.exception.ApiException
import ru.hnau.androidutils.preferences.PreferencesManager
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.SimpleProducer
import ru.hnau.jutils.producer.callListeners
import ru.hnau.jutils.tryOrElse


object AuthManager : PreferencesManager("auth") {

    var adminAuthToken by newStringProperty("admin_auth_token")
    var raterCode by newStringProperty("rater_code")

    val isAdmin get() = adminAuthToken.isNotEmpty()

    val isRater get() = raterCode.isNotEmpty()

    val isLogged get() = isAdmin || isRater

    private val onUserLoggedProducerInner = SimpleProducer<Unit>()
    val onUserLoggedProducer: Producer<Unit>
        get() = onUserLoggedProducerInner

    suspend fun loginAsAdmin(
        password: String
    ) {
        adminAuthToken = API.adminLogin(password).await()
        onUserLoggedProducerInner.callListeners()
    }

    fun loginAsRater(
        raterCode: String
    ) {
        this.raterCode = raterCode
        onUserLoggedProducerInner.callListeners()
    }

    fun logout() {
        adminAuthToken = ""
        raterCode = ""
    }

}