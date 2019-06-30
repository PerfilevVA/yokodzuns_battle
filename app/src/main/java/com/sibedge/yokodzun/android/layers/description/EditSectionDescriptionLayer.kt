package com.sibedge.yokodzun.android.layers.description

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.admin.AdminAllBattlesDataManager
import com.sibedge.yokodzun.android.layers.description.base.EditDescriptionLayer
import com.sibedge.yokodzun.android.layers.sections.edit.SectionsEditingCallback
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState


class EditSectionDescriptionLayer(
    context: Context
) : EditDescriptionLayer(
    context = context,
    titleHint = StringGetter(R.string.edit_section_description_layer_title_hint),
    logoUrlHint = StringGetter(R.string.edit_section_description_layer_logo_url_hint),
    descriptionHint = StringGetter(R.string.edit_section_description_layer_description_hint)
) {

    companion object {

        fun newInstance(
            context: Context,
            section: Section,
            callback: SectionsEditingCallback
        ) = EditSectionDescriptionLayer(context).apply {
            this.section = section
            this.callback = callback
        }

    }

    @LayerState
    private lateinit var section: Section

    @LayerState
    private lateinit var callback: SectionsEditingCallback

    override val title = StringGetter(R.string.edit_section_description_layer_title)

    override val initialDescription get() = section.description

    override suspend fun saveAsync(editedDescription: Description) =
        callback.updateDescription(section.id, editedDescription)

}