package com.sibedge.yokodzun.android.data

import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.preferences.PreferencesManager
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.SimpleProducer
import ru.hnau.jutils.producer.callListeners
import ru.hnau.jutils.takeIfNotEmpty
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.layers.LoginLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector


object AuthManager : PreferencesManager("AUTH") {

    private val onUserLoggedProducerInner = SimpleProducer<Unit>()
    val onUserLoggedProducer: Producer<Unit>
        get() = onUserLoggedProducerInner

    private var tokenInner: String by newStringProperty("TOKEN")
    val token: String?
        get() = tokenInner.takeIfNotEmpty()

    private var loginInner: String by newStringProperty("LOGIN")
    val login: String?
        get() = loginInner.takeIfNotEmpty()

    val logged: Boolean
        get() = token != null

    suspend fun login(login: String, password: String) {
        tokenInner = API.login(login, password, AppInstanceManager.uuid).await()
        loginInner = login
        onUserLoggedProducerInner.callListeners()
    }

    fun logout(coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit) {
        coroutinesExecutor {
            API.logout(false, AppInstanceManager.uuid).await()
            tokenInner = ""
            AppActivityConnector.showLayer(::LoginLayer, true)
        }

    }


}