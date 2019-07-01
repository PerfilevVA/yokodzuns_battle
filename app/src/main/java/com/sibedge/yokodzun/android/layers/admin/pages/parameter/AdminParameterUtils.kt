package com.sibedge.parameter.android.layers.admin.pages.parameter

import com.sibedge.parameter.android.data.ParametersDataManager
import com.sibedge.parameter.android.layers.description.EditParameterDescriptionLayer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.Parameter
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object AdminParameterUtils {

    fun showParameterActions(
        parameter: Parameter,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showBottomSheet {
        title(parameter.entityNameWithTitle)
        closeItem(StringGetter(R.string.parameter_action_edit_description)) {
            AppActivityConnector.showLayer({
                EditParameterDescriptionLayer.newInstance(context, parameter)
            })
        }
        closeItem(StringGetter(R.string.parameter_action_remove)) {
            AppActivityConnector.showConfirmDialog(
                title = StringGetter(R.string.parameter_action_remove_confirm_dialog_title),
                text = StringGetter(R.string.parameter_action_remove_confirm_dialog_text),
                confirmText = StringGetter(R.string.dialog_remove)
            ) {
                coroutinesExecutor { ParametersDataManager.remove(parameterId = parameter.id) }
            }
        }
    }

}