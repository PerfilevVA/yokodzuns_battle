package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.common.exception.ApiException
import com.sibedge.yokodzun.common.utils.AuthUtils
import kotlinx.coroutines.Dispatchers
import ru.hnau.androidutils.preferences.PreferencesManager
import ru.hnau.jutils.coroutines.launch
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.SimpleProducer
import ru.hnau.jutils.producer.callListeners
import ru.hnau.jutils.takeIfNotEmpty
import ru.hnau.jutils.tryOrElse


object AuthManager : PreferencesManager("auth") {

    var adminAuthToken by newStringProperty("admin_auth_token")
    var raterCode by newStringProperty("rater_code")

    val isAdmin get() = adminAuthToken.isNotEmpty()
    val isRater get() = raterCode.isNotEmpty()
    val isLogged get() = isAdmin || isRater

    val login
        get() = raterCode.takeIfNotEmpty()
            ?: adminAuthToken.takeIfNotEmpty()?.let { AuthUtils.ADMIN_LOGIN }

    private val onUserLoggedProducerInner = SimpleProducer<Unit>()
    val onUserLoggedProducer: Producer<Unit>
        get() = onUserLoggedProducerInner

    suspend fun loginAsAdmin(
        password: String
    ) {
        adminAuthToken = API.adminLogin(
            password = password,
            appInstanceUUID = AppInstanceManager.uuid
        ).await()
        onUserLoggedProducerInner.callListeners()
    }

    suspend fun loginAsRater(
        raterCode: String
    ) {
        this.raterCode = raterCode
        try {
            API.raterLogin(AppInstanceManager.uuid)
            onUserLoggedProducerInner.callListeners()
        } catch (th: Throwable) {
            this.raterCode = ""
        }
    }

    fun logout() {
        adminAuthToken = ""
        raterCode = ""
        Dispatchers.IO.launch { API.onLogout(AppInstanceManager.uuid).await() }
    }

}