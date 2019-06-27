package com.sibedge.yokodzun.android.utils.managers.fcm

import android.app.PendingIntent
import android.content.Intent
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.utils.ContextConnector
import ru.hnau.androidutils.utils.generateId
import com.sibedge.yokodzun.android.AppActivity
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.TestsAttemptsForStudentManager
import com.sibedge.yokodzun.android.utils.managers.CrashliticsManager
import com.sibedge.yokodzun.android.utils.managers.notification.NotificationManager
import ru.hnau.remote_teaching_common.data.notification.RTNotification
import ru.hnau.remote_teaching_common.data.notification.type.RTNotificationCommon
import ru.hnau.remote_teaching_common.data.notification.type.RTNotificationOnTestAttemptAdd


object RTNotificationsHandler {

    fun onNewRTNotification(
        notification: RTNotification
    ) = when (notification) {

        is RTNotificationCommon -> {
            NotificationManager.show(
                text = notification.message.toGetter()
            )
        }

        is RTNotificationOnTestAttemptAdd -> {
            TestsAttemptsForStudentManager.invalidate()
            NotificationManager.show(
                text = StringGetter(R.string.notification_on_test_attempt_add, notification.testAttempt.testTitle),
                onClick = PendingIntent.getActivity(
                    ContextConnector.context,
                    generateId(),
                    AppActivity.createStartTestAttemptIntent(ContextConnector.context, notification.testAttempt.uuid),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }

        else -> {
            CrashliticsManager.handle("Unknown notification class ${notification.javaClass.name}")
        }

    }

}