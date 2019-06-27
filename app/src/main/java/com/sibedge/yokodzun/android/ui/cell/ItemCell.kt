package com.sibedge.yokodzun.android.ui.cell

import android.annotation.SuppressLint
import android.content.Context
import com.sibedge.yokodzun.android.ui.cell.line.LineCell
import com.sibedge.yokodzun.android.ui.cell.line.LineCellColor
import com.sibedge.yokodzun.android.utils.managers.ColorManager


@SuppressLint("ViewConstructor")
open class ItemCell<T : Any>(
    context: Context,
    onClick: ((T) -> Unit)? = null,
    dataGetter: (T) -> LineCell.Data<T>
) : LineCell<T>(
    context = context,
    onClick = onClick,
    activeColor = LineCellColor(
        titleColor = ColorManager.PRIMARY,
        subtitleColor = ColorManager.FG_T50
    ),
    inactiveColor = LineCellColor(
        titleColor = ColorManager.FG_T50,
        subtitleColor = ColorManager.FG_T50
    ),
    dataGetter = dataGetter,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
)