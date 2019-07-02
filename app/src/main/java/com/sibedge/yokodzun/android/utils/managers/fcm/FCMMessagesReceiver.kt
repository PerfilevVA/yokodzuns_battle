package com.sibedge.yokodzun.android.utils.managers.fcm

import com.google.firebase.messaging.RemoteMessage
import com.sibedge.yokodzun.android.data.AuthManager
import ru.hnau.jutils.takeIfNotEmpty
import ru.hnau.jutils.tryCatch
import com.sibedge.yokodzun.android.utils.managers.CrashliticsManager
import com.sibedge.yokodzun.common.data.notification.YNotification
import com.sibedge.yokodzun.common.data.notification.YToUserNotification
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException


object FCMMessagesReceiver {

    fun onNewFcmMessage(fcmMessage: RemoteMessage?) {

        val notification = tryCatch(
            throwsAction = { deserializeMessage(fcmMessage) },
            onThrow = { th ->
                CrashliticsManager.handle(
                    IllegalArgumentException(
                        "Unable decode YNotification",
                        th
                    )
                )
            }
        ) ?: return

        YNotificationsHandler.onNewYNotification(notification)

    }

    @Throws
    private fun deserializeMessage(message: RemoteMessage?): YNotification {

        val loggedUserLogin = AuthManager.login
            ?: throw IllegalStateException("user not isLogged")

        val data = message?.data
            ?: throw IllegalArgumentException("data is null")

        val toUser = data[YToUserNotification.SERIALIZATION_KEY_TO_USER]
            ?.takeIfNotEmpty()
            ?: throw IllegalArgumentException("toUser is empty")

        if (loggedUserLogin != toUser) {
            throw IllegalArgumentException("toUser is not equals isLogged user")
        }

        val clazz = data[YToUserNotification.SERIALIZATION_KEY_CLASS]
            ?.takeIfNotEmpty()
            ?: throw IllegalArgumentException("data class is empty")

        val content = data[YToUserNotification.SERIALIZATION_KEY_CONTENT]
            ?.takeIfNotEmpty()
            ?: throw IllegalArgumentException("data json is empty")

        return YNotification.deserialize(clazz, content)
    }

}