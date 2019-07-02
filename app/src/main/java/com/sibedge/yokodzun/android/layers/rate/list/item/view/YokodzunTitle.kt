package com.sibedge.yokodzun.android.layers.rate.list.item.view

import android.content.Context
import android.view.View
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItem
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.apply.applyBottomPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyTopPadding


class YokodzunTitle(
    context: Context
) : Label(
    context = context,
    textColor = ColorManager.PRIMARY,
    textSize = SizeManager.TEXT_16,
    fontType = FontManager.DEFAULT,
    gravity = HGravity.START_CENTER_VERTICAL,
    minLines = 1,
    maxLines = 1
), BaseListViewWrapper<RatesListItem> {

    override val view = this

    init {
        applyHorizontalPadding(SizeManager.DEFAULT_SEPARATION)
        applyBottomPadding(SizeManager.SMALL_SEPARATION)
        applyTopPadding(SizeManager.DEFAULT_SEPARATION)
    }

    override fun setContent(content: RatesListItem, position: Int) {
        text = content.yokodzun!!.description.title.toGetter()
    }

}