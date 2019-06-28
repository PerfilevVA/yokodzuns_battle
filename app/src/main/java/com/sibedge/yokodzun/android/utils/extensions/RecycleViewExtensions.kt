package com.sibedge.yokodzun.android.utils.extensions

import androidx.recyclerview.widget.RecyclerView
import ru.hnau.androidutils.context_getters.dp_px.dp112
import ru.hnau.androidutils.ui.view.list.base.BaseListPaddingDecoration


fun RecyclerView.setBottomPaddingForPrimaryActionButtonDecoration() =
    addItemDecoration(BaseListPaddingDecoration(afterLast = dp112))