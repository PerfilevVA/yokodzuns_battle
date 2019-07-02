package com.sibedge.yokodzun.android.layers.sections.base

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.sections.content.OpenedSections
import com.sibedge.yokodzun.android.ui.view.list.sections.content.SectionsList
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.jutils.producer.extensions.toProducer


abstract class ImmutableSectionsLayer(context: Context) : SectionsLayer(context) {

    protected abstract val openedSections: OpenedSections

    override val sectionsList by lazy {
        SectionsList(
            sections = battle.sections.toProducer(),
            openedSections = openedSections
        )
    }

    override val emptyInfoView by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.sections_layer_no_sections_title)
        )
    }

}