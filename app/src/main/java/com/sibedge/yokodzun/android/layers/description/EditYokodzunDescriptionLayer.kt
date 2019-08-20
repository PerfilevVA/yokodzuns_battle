package com.sibedge.yokodzun.android.layers.description

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.YokodzunsDataManager
import com.sibedge.yokodzun.android.layers.description.base.EditDescriptionLayer
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState


class EditYokodzunDescriptionLayer(
    context: Context
) : EditDescriptionLayer(
    context = context,
    titleHint = StringGetter(R.string.edit_team_description_layer_title_hint),
    logoUrlHint = StringGetter(R.string.edit_team_description_layer_logo_url_hint),
    descriptionHint = StringGetter(R.string.edit_team_description_layer_description_hint)
) {

    companion object {

        fun newInstance(
            context: Context,
            yokodzun: Yokodzun
        ) = EditYokodzunDescriptionLayer(context).apply {
            this.yokodzun = yokodzun
        }

    }

    @LayerState
    private lateinit var yokodzun: Yokodzun

    override val title = StringGetter(R.string.edit_team_description_layer_title)

    override val initialDescription get() = yokodzun.description

    override suspend fun saveAsync(editedDescription: Description) {
        YokodzunsDataManager.updateDescription(yokodzun.id, editedDescription)
    }

}