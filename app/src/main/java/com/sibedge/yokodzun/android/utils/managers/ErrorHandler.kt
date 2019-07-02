package com.sibedge.yokodzun.android.utils.managers

import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.utils.ContextConnector
import ru.hnau.androidutils.utils.shortToast
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.layers.ChangePasswordLayer
import com.sibedge.yokodzun.android.layers.SettingsLayer
import com.sibedge.yokodzun.android.layers.login.LoginLayer
import com.sibedge.yokodzun.common.exception.ApiException
import com.sibedge.yokodzun.common.exception.ApiExceptionContent
import com.sibedge.yokodzun.common.utils.uiString
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.logE
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.stringStackTrace
import java.lang.IllegalStateException


object ErrorHandler : (Throwable) -> Unit {

    fun handle(message: StringGetter) {
        val text = message.get(ContextConnector.context)
        val exception = IllegalStateException(text)
        handle(exception)
    }

    override fun invoke(th: Throwable) =
        handle(th)

    fun handle(th: Throwable) {

        logE(th.stringStackTrace)

        CrashliticsManager.handle(th)

        val apiException =
            th as? ApiException ?: ApiException.UNDEFINED

        val content = apiException.content
        when (content) {
            /*

            is ApiExceptionContent.SectionHasChildren -> TODO()

            is ApiExceptionContent.UnsupportedVersion -> TODO()

            is ApiExceptionContent.DdosBlocked -> TODO()

            is ApiExceptionContent.UserWithLoginAlreadyExists -> TODO()

            is ApiExceptionContent.IncorrectActionCode -> TODO()

            */

            is ApiExceptionContent.Authentication -> onAuthError()
            is ApiExceptionContent.AdminPasswordNotConfigured -> onAdminPasswordNotConfigured()
            is ApiExceptionContent.HostNotConfigured -> onHostNotConfigured()
            is ApiExceptionContent.Common -> displayError(content.message.toGetter())
            is ApiExceptionContent.DdosBlocked -> onDdosError(content.secondsToUnlock)
            else -> displayError(StringGetter(R.string.error_undefined))

        }


    }

    private fun displayError(message: StringGetter) =
        shortToast(message)

    private fun onDdosError(secondsToUnlock: Long?) {
        AppActivityConnector.showDialog {
            title(StringGetter(R.string.error_undefined))
            val timeToUnlockSuffix = secondsToUnlock?.let { seconds ->
                StringGetter("\n") + StringGetter(
                    R.string.error_ddos_seconds_to_unlock,
                    TimeValue(seconds).uiString
                )
            } ?: StringGetter.EMPTY
            text(StringGetter(R.string.error_ddos) + timeToUnlockSuffix)
            closeButton(StringGetter(R.string.dialog_close))
        }
    }

    private fun onAuthError() {
        displayError(StringGetter(R.string.error_authentication))
        AppActivityConnector.showLayer(::LoginLayer, true)
    }

    private fun onAdminPasswordNotConfigured() {
        AppActivityConnector.showDialog {
            title(StringGetter(R.string.default_admin_password_error_dialog_title))
            text(StringGetter(R.string.default_admin_password_error_dialog_text))
            closeButton(StringGetter(R.string.default_admin_password_error_dialog_button)) {
                AppActivityConnector.showLayer(::ChangePasswordLayer)
            }
        }
    }

    private fun onHostNotConfigured() {
        AppActivityConnector.showDialog {
            title(StringGetter(R.string.host_not_configured_error_dialog_title))
            text(StringGetter(R.string.host_not_configured_error_dialog_text))
            closeButton(StringGetter(R.string.host_not_configured_error_dialog_button)) {
                AppActivityConnector.showLayer(::SettingsLayer)
            }
        }
    }

}