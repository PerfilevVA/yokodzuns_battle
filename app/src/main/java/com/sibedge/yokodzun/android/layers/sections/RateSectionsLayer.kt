package com.sibedge.yokodzun.android.layers.sections

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.rate.RateSectionLayer
import com.sibedge.yokodzun.android.layers.sections.base.ImmutableSectionsLayer
import com.sibedge.yokodzun.android.layers.sections.edit.AdminEditSectionsLayer
import com.sibedge.yokodzun.android.layers.sections.edit.SectionsEditor
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.ui.view.list.sections.content.OpenedSections
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.jutils.handle


class RateSectionsLayer(
    context: Context
) : ImmutableSectionsLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = RateSectionsLayer(context).apply {
            this.battle = battle
            this.openedSections = OpenedSections()
        }

    }

    @LayerState
    override lateinit var battle: Battle

    @LayerState
    override lateinit var openedSections: OpenedSections

    override val additionalButtonInfoCreator = { section: Section ->
        (section.weight > 0).handle(
            onTrue = {
                AdditionalButton.Info(DrawableGetter(R.drawable.ic_rate_half_fg)) {
                    AppActivityConnector.showLayer({
                        RateSectionLayer.newInstance(context, section)
                    })
                }
            },
            onFalse = { null }
        )
    }

}