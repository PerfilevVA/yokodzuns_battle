package com.sibedge.parameter.android.layers.description

import android.content.Context
import com.sibedge.parameter.android.data.ParametersDataManager
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.description.base.EditDescriptionLayer
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState


class EditParameterDescriptionLayer(
    context: Context
) : EditDescriptionLayer(
    context = context,
    titleHint = StringGetter(R.string.edit_parameter_description_layer_title_hint),
    logoUrlHint = StringGetter(R.string.edit_parameter_description_layer_logo_url_hint),
    descriptionHint = StringGetter(R.string.edit_parameter_description_layer_description_hint)
) {

    companion object {

        fun newInstance(
            context: Context,
            parameter: Parameter
        ) = EditParameterDescriptionLayer(context).apply {
            this.parameter = parameter
        }

    }

    @LayerState
    private lateinit var parameter: Parameter

    override val title = StringGetter(R.string.edit_parameter_description_layer_title)

    override val initialDescription get() = parameter.description

    override suspend fun saveAsync(editedDescription: Description) {
        ParametersDataManager.updateDescription(parameter.id, editedDescription)
    }

}