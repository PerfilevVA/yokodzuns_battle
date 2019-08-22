package com.sibedge.yokodzun.android.layers.description

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.TeamsDataManager
import com.sibedge.yokodzun.android.layers.description.base.EditDescriptionLayer
import com.sibedge.yokodzun.common.data.Team
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState


class EditTeamDescriptionLayer(
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
            team: Team
        ) = EditTeamDescriptionLayer(context).apply {
            this.team = team
        }

    }

    @LayerState
    private lateinit var team: Team

    override val title = StringGetter(R.string.edit_team_description_layer_title)

    override val initialDescription get() = team.description

    override suspend fun saveAsync(editedDescription: Description) {
        TeamsDataManager.updateDescription(team.id, editedDescription)
    }

}