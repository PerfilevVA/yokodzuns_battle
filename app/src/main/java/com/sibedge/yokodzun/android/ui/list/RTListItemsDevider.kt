package com.sibedge.yokodzun.android.ui.list

import android.content.Context
import ru.hnau.androidutils.context_getters.dp_px.dp1
import ru.hnau.androidutils.ui.view.list.base.BaseListItemsDivider
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


object RTListItemsDevider {

    fun create(
        context: Context
    ) = BaseListItemsDivider(
        context = context,
        color = ColorManager.FG.mapWithAlpha(0.1f),
        size = dp1,
        paddingStart = SizeManager.DEFAULT_SEPARATION,
        paddingEnd = SizeManager.DEFAULT_SEPARATION
    )

}