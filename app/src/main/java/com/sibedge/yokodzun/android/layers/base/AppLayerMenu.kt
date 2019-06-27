package com.sibedge.yokodzun.android.layers.base

import android.view.View
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.ProfileLayer
import com.sibedge.yokodzun.android.layers.SettingsLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.OptionsMenuManager


object AppLayerMenu {

    fun show(
        anchor: View,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) =
        OptionsMenuManager.show(anchor, getItems(coroutinesExecutor))

    private fun getItems(
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) =
        getAnonymousItems() +
                (if (AuthManager.logged) getProfileItems(coroutinesExecutor) else emptyList())

    private fun getAnonymousItems() = listOf(
        OptionsMenuManager.Item(
            title = StringGetter(R.string.options_menu_item_settings),
            onClick = { AppActivityConnector.showLayer(::SettingsLayer) }
        )
    )

    private fun getProfileItems(
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = listOf(
        OptionsMenuManager.Item(
            title = StringGetter(R.string.options_menu_item_profile),
            onClick = {
                coroutinesExecutor {
                    val user = MeInfoManager.wait().get()
                    AppActivityConnector.showLayer({ context ->
                        ProfileLayer.newInstance(context, user)
                    })
                }
            }
        ),
        OptionsMenuManager.Item(
            title = StringGetter(R.string.options_menu_item_logout),
            onClick = { askAndLogout(coroutinesExecutor) }
        )
    )

    private fun askAndLogout(
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) =
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.options_menu_ask_logout_title),
            text = StringGetter(R.string.options_menu_ask_logout_text),
            confirmText = StringGetter(R.string.options_menu_confirm_logout),
            onConfirm = { AuthManager.logout(coroutinesExecutor) }
        )

}