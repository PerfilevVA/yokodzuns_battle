package com.sibedge.yokodzun.android.utils.managers.fcm

import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.hnau.androidutils.preferences.PreferencesManager
import ru.hnau.jutils.coroutines.launch
import ru.hnau.jutils.takeIfNotEmpty
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.data.AppInstanceManager
import com.sibedge.yokodzun.android.utils.managers.CrashliticsManager
import com.sibedge.yokodzun.android.utils.managers.SettingsManager


class FCMManager : FirebaseMessagingService() {

    companion object : PreferencesManager("FIREBASE_MESSAGES") {

        private var tokenKey by newStringProperty("TOKEN_KEY")

        init {
            SettingsManager.hostProperty.attach { sendPushTokenIfNeed() }
        }

        fun sendPushTokenIfNeed() {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener() { task ->
                onTokenTaskCompleted(task)
            }
        }

        private fun onTokenTaskCompleted(task: Task<InstanceIdResult>) {
            if (!task.isSuccessful) {
                CrashliticsManager.handle(
                    Exception(
                        "Fcm pushToken receiving error",
                        task.exception
                    )
                )
                return
            }

            val token = task.result?.token?.takeIfNotEmpty()
            if (token == null) {
                CrashliticsManager.handle("Token is empty")
                return
            }

            onTokenChanged(token)
        }

        private val sendTokenMutex = Mutex()

        private fun onTokenChanged(token: String) {
            val tokenKey = PushToken.createKey(token)
            Dispatchers.IO.launch {
                sendTokenMutex.withLock {
                    if (FCMManager.tokenKey == tokenKey) {
                        return@launch
                    }
                    try {
                        //TODO API.updatePushToken(token, AppInstanceManager.uuid).await()
                        FCMManager.tokenKey = tokenKey
                    } catch (th: Throwable) {
                        CrashliticsManager.handle(th)
                    }
                }
            }
        }

    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        tokenKey = ""
        token ?: return
        onTokenChanged(token)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        FCMMessagesReceiver.onNewFcmMessage(message)
    }

}