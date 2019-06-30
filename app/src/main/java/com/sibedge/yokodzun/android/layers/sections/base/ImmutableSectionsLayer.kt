package com.sibedge.yokodzun.android.layers.sections.base

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import ru.hnau.androidutils.context_getters.StringGetter


abstract class ImmutableSectionsLayer(context: Context) : SectionsLayer(context) {

    override val emptyInfoView by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.sections_layer_no_sections_title)
        )
    }

}