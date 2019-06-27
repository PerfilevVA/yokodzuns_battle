package com.sibedge.yokodzun.android.ui.cell

import android.annotation.SuppressLint
import android.content.Context
import com.sibedge.yokodzun.android.ui.cell.line.LineCell
import com.sibedge.yokodzun.android.ui.cell.line.LineCellColor
import com.sibedge.yokodzun.android.utils.managers.ColorManager


@SuppressLint("ViewConstructor")
open class TitleCell<T : Any>(
    context: Context,
    dataGetter: (T) -> LineCell.Data<T>
) : LineCell<T>(
    context = context,
    activeColor = LineCellColor(
        titleColor = ColorManager.BG,
        subtitleColor = ColorManager.BG_T50
    ),
    inactiveColor = LineCellColor(
        titleColor = ColorManager.BG_T50,
        subtitleColor = ColorManager.BG_T50
    ),
    dataGetter = dataGetter,
    rippleDrawInfo = ColorManager.BG_ON_PRIMARY_RIPPLE_INFO
)