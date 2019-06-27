package com.sibedge.yokodzun.android.utils.managers.fcm

import com.google.firebase.messaging.RemoteMessage
import ru.hnau.jutils.takeIfNotEmpty
import ru.hnau.jutils.tryCatch
import ru.hnau.jutils.tryOrNull
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.utils.managers.CrashliticsManager
import com.sibedge.yokodzun.android.utils.tryOrLogToCrashlitics
import ru.hnau.remote_teaching_common.data.notification.RTNotification
import ru.hnau.remote_teaching_common.data.notification.RTToUserNotification
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException


object FCMMessagesReceiver {

    fun onNewFcmMessage(fcmMessage: RemoteMessage?) {

        val notification = tryCatch(
                throwsAction = { deserializeMessage(fcmMessage) },
                onThrow = { th ->
                    CrashliticsManager.handle(
                            IllegalArgumentException(
                                    "Unable decode RTNotification",
                                    th
                            )
                    )
                }
        ) ?: return

        RTNotificationsHandler.onNewRTNotification(notification)

    }

    @Throws
    private fun deserializeMessage(message: RemoteMessage?): RTNotification {

        val loggedUserLogin = AuthManager.login
                ?.takeIf { AuthManager.logged }
                ?: throw IllegalStateException("user not logged")

        val data = message?.data
                ?: throw IllegalArgumentException("data is null")

        val toUser = data[RTToUserNotification.SERIALIZATION_KEY_TO_USER]
                ?.takeIfNotEmpty()
                ?: throw IllegalArgumentException("toUser is empty")

        if (loggedUserLogin != toUser) {
            throw IllegalArgumentException("toUser is not equals logged user")
        }

        val clazz = data[RTToUserNotification.SERIALIZATION_KEY_CLASS]
                ?.takeIfNotEmpty()
                ?: throw IllegalArgumentException("content class is empty")

        val content = data[RTToUserNotification.SERIALIZATION_KEY_CONTENT]
                ?.takeIfNotEmpty()
                ?: throw IllegalArgumentException("content json is empty")

        return RTNotification.deserialize(clazz, content)
    }

}