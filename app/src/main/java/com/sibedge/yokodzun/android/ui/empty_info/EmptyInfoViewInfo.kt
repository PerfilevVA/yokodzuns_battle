package com.sibedge.yokodzun.android.ui.empty_info

import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.LabelInfo
import com.sibedge.yokodzun.android.ui.button.YButtonInfo
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


data class EmptyInfoViewInfo(
    val title: LabelInfo = LabelInfo(
        fontType = FontManager.DEFAULT,
        textSize = SizeManager.TEXT_16,
        textColor = ColorManager.PRIMARY,
        gravity = HGravity.CENTER
    ),
    val buttonInfo: YButtonInfo = YButtonInfo.SMALL_PRIMARY_TEXT_AND_BORDER,
    val paddingHorizontal: DpPxGetter = SizeManager.LARGE_SEPARATION,
    val paddingVertical: DpPxGetter = SizeManager.LARGE_SEPARATION,
    val titleButtonSeparation: DpPxGetter = SizeManager.DEFAULT_SEPARATION
) {

    companion object {

        val DEFAULT = EmptyInfoViewInfo()

    }

}