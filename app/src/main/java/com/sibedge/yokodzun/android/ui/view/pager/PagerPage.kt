package com.sibedge.yokodzun.android.ui.view.pager

import android.view.View
import ru.hnau.androidutils.context_getters.StringGetter


data class PagerPage(
    val title: StringGetter,
    val viewCreator: () -> View
)