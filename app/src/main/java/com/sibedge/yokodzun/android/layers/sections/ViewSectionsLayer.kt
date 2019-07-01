package com.sibedge.yokodzun.android.layers.sections

import android.content.Context
import com.sibedge.yokodzun.android.layers.sections.base.ImmutableSectionsLayer
import com.sibedge.yokodzun.android.layers.sections.edit.AdminEditSectionsLayer
import com.sibedge.yokodzun.android.layers.sections.edit.SectionsEditor
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.ui.view.layer.layer.LayerState


class ViewSectionsLayer(
    context: Context
) : ImmutableSectionsLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = ViewSectionsLayer(context).apply {
            this.battle = battle
        }

    }

    @LayerState
    override lateinit var battle: Battle

}