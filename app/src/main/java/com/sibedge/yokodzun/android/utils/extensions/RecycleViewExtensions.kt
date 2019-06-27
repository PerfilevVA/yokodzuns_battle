package com.sibedge.yokodzun.android.utils.extensions

import android.support.v7.widget.RecyclerView
import ru.hnau.androidutils.context_getters.dp_px.dp112
import ru.hnau.androidutils.ui.view.list.base.BaseListPaddingDecoration


fun RecyclerView.setBottomPaddingForMainActionButtonDecoration() =
    addItemDecoration(BaseListPaddingDecoration(afterLast = dp112))