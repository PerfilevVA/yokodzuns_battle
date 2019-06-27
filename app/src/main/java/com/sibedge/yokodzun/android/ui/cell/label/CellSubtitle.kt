package com.sibedge.yokodzun.android.ui.cell.label

import android.content.Context
import ru.hnau.androidutils.context_getters.ColorGetter
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class CellSubtitle(
    context: Context,
    activeColor: ColorGetter,
    inactiveColor: ColorGetter
) : CellLabel(
    context = context,
    activeColor = activeColor,
    inactiveColor = inactiveColor,
    textSize = SizeManager.TEXT_12
)