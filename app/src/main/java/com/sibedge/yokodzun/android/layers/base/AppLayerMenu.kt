package com.sibedge.yokodzun.android.layers.base

import android.view.View
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.layers.ChangePasswordLayer
import com.sibedge.yokodzun.android.layers.SettingsLayer
import com.sibedge.yokodzun.android.layers.login.LoginLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.OptionsMenuManager
import ru.hnau.androidutils.context_getters.StringGetter
import java.util.*


object AppLayerMenu {

    fun show(
        anchor: View
    ) = OptionsMenuManager.show(
        anchor = anchor,
        items = getItems()
    )

    private fun getItems(): List<OptionsMenuManager.Item> {

        val result = LinkedList<OptionsMenuManager.Item>()

        if (AuthManager.isAdmin) {
            result.addAll(getAdminItems())
        }

        result.addAll(getCommonItems())

        if (AuthManager.isLogged) {
            result.addAll(getLoggedItems())
        }

        return result

    }

    private fun getCommonItems() = listOf(
        OptionsMenuManager.Item(
            title = StringGetter(R.string.global_menu_item_title_settings),
            onClick = { AppActivityConnector.showLayer(::SettingsLayer) }
        )
    )

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