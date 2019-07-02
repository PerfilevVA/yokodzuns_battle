package com.sibedge.yokodzun.android.layers.rate.list.item.view

import android.content.Context
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItem
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding


class ParameterTitle(
    context: Context
) : Label(
    context = context,
    textColor = ColorManager.FG,
    textSize = SizeManager.TEXT_12,
    fontType = FontManager.DEFAULT,
    gravity = HGravity.START_CENTER_VERTICAL,
    minLines = 1,
    maxLines = 1
), BaseListViewWrapper<RatesListItem> {

    override val view = this

    init {
        applyPadding(SizeManager.DEFAULT_SEPARATION, SizeManager.SMALL_SEPARATION)
    }

    override fun setContent(content: RatesListItem, position: Int) {
        text = content.parameter!!.description.title.toGetter()
    }

}