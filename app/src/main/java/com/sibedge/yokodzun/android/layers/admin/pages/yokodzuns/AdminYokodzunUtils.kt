package com.sibedge.yokodzun.android.layers.admin.pages.yokodzuns

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.BattlesDataManager
import com.sibedge.yokodzun.android.data.YokodzunsDataManager
import com.sibedge.yokodzun.android.layers.description.EditYokodzunDescriptionLayer
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.Yokodzun
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object AdminYokodzunUtils {

    fun showYokodzunActions(
        yokodzun: Yokodzun,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showBottomSheet {
        title(yokodzun.entityNameWithTitle)
        closeItem(StringGetter(R.string.yokodzun_action_edit_description)) {
            AppActivityConnector.showLayer({
                EditYokodzunDescriptionLayer.newInstance(context, yokodzun)
            })
        }
        closeItem(StringGetter(R.string.yokodzun_action_remove)) {
            AppActivityConnector.showConfirmDialog(
                title = StringGetter(R.string.yokodzun_action_remove_confirm_dialog_title),
                text = StringGetter(R.string.yokodzun_action_remove_confirm_dialog_text),
                confirmText = StringGetter(R.string.dialog_remove)
            ) {
                coroutinesExecutor { YokodzunsDataManager.remove(yokodzunId = yokodzun.id) }
            }
        }
    }

}