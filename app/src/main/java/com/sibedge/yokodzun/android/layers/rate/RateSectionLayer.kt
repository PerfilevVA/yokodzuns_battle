package com.sibedge.yokodzun.android.layers.rate

import android.content.Context
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.raters.RatersLayer
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState


class RateSectionLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            section: Section
        ) = RateSectionLayer(context).apply {
            this.section = section
        }

    }

    @LayerState
    private lateinit var section: Section

    override val title get() = section.entityNameWithTitle

}