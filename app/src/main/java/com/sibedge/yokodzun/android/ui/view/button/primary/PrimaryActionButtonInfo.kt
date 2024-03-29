package com.sibedge.yokodzun.android.ui.view.button.primary

import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.animations.AnimationsUtils
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter
import ru.hnau.androidutils.context_getters.dp_px.dp56
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.drawer.shadow.info.ButtonShadowInfo
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.jutils.TimeValue


data class PrimaryActionButtonInfo(
    val minSize: DpPxGetter = dp56,
    val rippleDrawInfo: RippleDrawInfo = ColorManager.FG_ON_TRANSPARENT_RIPPLE_INFO,
    val shadowInfo: ButtonShadowInfo = ColorManager.DEFAULT_BUTTON_SHADOW_INFO,
    val animatingTime: TimeValue = AnimationsUtils.DEFAULT_ANIMATION_TIME,
    val titleLabelInfo: LabelInfo = LabelInfo(
        textColor = ColorManager.FG,
        fontType = FontManager.DEFAULT,
        textSize = SizeManager.TEXT_16
    ),
    val backgroundColor: ColorTriple = ColorManager.PRIMARY_TRIPLE
) {

    companion object {

        val DEFAULT = PrimaryActionButtonInfo()

    }

}