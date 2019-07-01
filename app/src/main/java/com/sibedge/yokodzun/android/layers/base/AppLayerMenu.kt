package com.sibedge.yokodzun.android.layers.base

import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.View
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.layers.ChangePasswordLayer
import com.sibedge.yokodzun.android.layers.login.LoginLayer
import com.sibedge.yokodzun.android.layers.SettingsLayer
import com.sibedge.yokodzun.android.layers.admin.AdminLayer
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.OptionsMenuManager
import com.sibedge.yokodzun.android.utils.tryOrHandleError
import com.sibedge.yokodzun.common.utils.Validators
import java.util.*


object AppLayerMenu {

    fun show(
        anchor: View,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = OptionsMenuManager.show(
        anchor = anchor,
        items = getItems(coroutinesExecutor)
    )

    private fun getItems(
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ): List<OptionsMenuManager.Item> {

        val result = LinkedList<OptionsMenuManager.Item>()

        if (AuthManager.isAdmin) {
            result.addAll(getAdminItems())
        }

        result.addAll(getCommonItems())

        if (AuthManager.isLogged) {
            result.addAll(getLoggedItems())
        } else {
            result.addAll(getAnonymousItems(coroutinesExecutor))
        }

        return result

    }

    private fun getCommonItems() = listOf(
        OptionsMenuManager.Item(
            title = StringGetter(R.string.global_menu_item_title_settings),
            onClick = { AppActivityConnector.showLayer(::SettingsLayer) }
        )
    )

    private fun getAnonymousItems(
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = listOf(
        OptionsMenuManager.Item(
            title = StringGetter(R.string.global_menu_item_title_login_as_admin),
            onClick = { loginAsAdmin(coroutinesExecutor) }
        )
    )

    private fun loginAsAdmin(
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showInputDialog(
        title = StringGetter(R.string.global_menu_login_as_admin_dialog_title),
        text = StringGetter(R.string.global_menu_login_as_admin_dialog_text),
        inputHint = StringGetter(R.string.global_menu_login_as_admin_dialog_hint),
        confirmButtonText = StringGetter(R.string.global_menu_login_as_admin_dialog_login_button),
        inputInfo = SimpleInputViewInfo(
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
            transformationMethod = PasswordTransformationMethod.getInstance()
        )
    ) { password ->
        tryOrHandleError {
            Validators.validatePasswordOrThrow(password)
            loginAsAdminWithPassword(password, coroutinesExecutor)
            true
        } ?: false
    }

    private fun loginAsAdminWithPassword(
        password: String,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = coroutinesExecutor {
        AuthManager.loginAsAdmin(password)
        AppActivityConnector.showLayer(::AdminLayer, true)
    }

    private fun getLoggedItems() = listOf(
        OptionsMenuManager.Item(
            title = StringGetter(R.string.global_menu_item_title_logout),
            onClick = {
                AppActivityConnector.showConfirmDialog(
                    title = StringGetter(R.string.global_menu_logout_ask_title),
                    text = StringGetter(R.string.global_menu_logout_ask_text),
                    confirmText = StringGetter(R.string.global_menu_logout_confirm)
                ) {
                    AuthManager.logout()
                    AppActivityConnector.showLayer(::LoginLayer, true)
                }
            }
        )
    )

    private fun getAdminItems() = listOf(
        OptionsMenuManager.Item(
            title = StringGetter(R.string.global_menu_item_title_admin_change_password),
            onClick = { AppActivityConnector.showLayer(::ChangePasswordLayer) }
        )
    )

}