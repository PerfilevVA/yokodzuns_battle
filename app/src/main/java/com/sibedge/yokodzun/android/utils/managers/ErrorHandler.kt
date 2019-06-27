package com.sibedge.yokodzun.android.utils.managers

import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.utils.ContextConnector
import ru.hnau.androidutils.utils.shortToast
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.ChangePasswordLayer
import com.sibedge.yokodzun.android.layers.LoginLayer
import com.sibedge.yokodzun.android.layers.SettingsLayer
import ru.hnau.remote_teaching_common.exception.ApiException
import ru.hnau.remote_teaching_common.exception.ApiExceptionContent
import java.lang.IllegalStateException


object ErrorHandler: (Throwable) -> Unit {

    fun handle(message: StringGetter) {
        val text = message.get(ContextConnector.context)
        val exception = IllegalStateException(text)
        handle(exception)
    }

    override fun invoke(th: Throwable) =
            handle(th)

    fun handle(th: Throwable) {

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

            */

            is ApiExceptionContent.IncorrectActionCode -> onIncorrectActionCode()
            is ApiExceptionContent.Authentication -> onAuthError()
            is ApiExceptionContent.AdminPasswordNotConfigured -> onAdminPasswordNotConfigured()
            is ApiExceptionContent.HostNotConfigured -> onHostNotConfigured()
            is ApiExceptionContent.Common -> displayError(
                content.message.toGetter()
            )
            else -> displayError(StringGetter(R.string.error_undefined))

        }


    }

    private fun displayError(message: StringGetter) =
        shortToast(message)

    private fun onIncorrectActionCode() =
        displayError(StringGetter(R.string.error_incorrect_action_code))

    private fun onAuthError() {
        if (!AuthManager.logged) {
            displayError(StringGetter(R.string.error_incorrect_login_or_password))
            return
        }
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