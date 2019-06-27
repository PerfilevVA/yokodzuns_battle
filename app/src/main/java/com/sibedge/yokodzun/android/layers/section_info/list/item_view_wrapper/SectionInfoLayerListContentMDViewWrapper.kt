package com.sibedge.yokodzun.android.layers.section_info.list.item_view_wrapper

import android.content.Context
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.setPadding
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerListItem
import com.sibedge.yokodzun.android.ui.cell.Cell
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class SectionInfoLayerListContentMDViewWrapper(
    context: Context
) : Cell<SectionInfoLayerListItem>(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
) {

    private val contentMDLabel = Label(
        context = context,
        fontType = FontManager.DEFAULT,
        gravity = HGravity.START_CENTER_VERTICAL,
        textSize = SizeManager.TEXT_16,
        textColor = ColorManager.FG
    )

    init {
        setPadding(SizeManager.DEFAULT_SEPARATION)
        addChild(contentMDLabel)
    }

    override fun onContentReceived(content: SectionInfoLayerListItem) {
        contentMDLabel.text = (content.value as String).toGetter()
    }

}