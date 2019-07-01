package com.sibedge.yokodzun.android.layers.description

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.BattlesDataManager
import com.sibedge.yokodzun.android.layers.description.base.EditDescriptionLayer
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState


class EditBattleDescriptionLayer(
    context: Context
) : EditDescriptionLayer(
    context = context,
    titleHint = StringGetter(R.string.edit_battle_description_layer_title_hint),
    logoUrlHint = StringGetter(R.string.edit_battle_description_layer_logo_url_hint),
    descriptionHint = StringGetter(R.string.edit_battle_description_layer_description_hint)
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = EditBattleDescriptionLayer(context).apply {
            this.battle = battle
        }

    }

    @LayerState
    private lateinit var battle: Battle

    override val title = StringGetter(R.string.edit_battle_description_layer_title)

    override val initialDescription get() = battle.description

    override suspend fun saveAsync(editedDescription: Description) {
        BattlesDataManager.updateDescription(battle.id, editedDescription)
    }

}