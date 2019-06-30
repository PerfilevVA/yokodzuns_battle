package com.sibedge.yokodzun.android.ui.button

import android.graphics.Color
import com.sibedge.yokodzun.android.utils.ColorTriple
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.dp_px.*
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter.Companion.dp
import ru.hnau.androidutils.ui.drawer.border.BorderInfo
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.drawer.shadow.info.ButtonShadowInfo
import ru.hnau.androidutils.ui.font_type.FontTypeGetter
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.android.utils.toTriple


data class YButtonInfo(
    val textSize: DpPxGetter = SizeManager.TEXT_16,
    val shadow: ButtonShadowInfo? = null,
    val height: DpPxGetter,
    val paddingHorizontal: DpPxGetter = height,
    val font: FontTypeGetter = FontManager.DEFAULT,
    val borderInfo: BorderInfo? = null,
    val textColor: ColorGetter,
    val underline: Boolean = false,
    val backgroundColor: ColorTriple
) {

    companion object {

        val LARGE_PRIMARY_BACKGROUND_SHADOW = YButtonInfo(
            shadow = ColorManager.DEFAULT_BUTTON_SHADOW_INFO,
            height = dp(44),
            textColor = ColorManager.FG,
            backgroundColor = ColorManager.PRIMARY_TRIPLE
        )

        val SMALL_FG_TEXT_UNDERLINE = YButtonInfo(
            textSize = SizeManager.TEXT_12,
            height = dp40,
            textColor = ColorManager.FG,
            backgroundColor = ColorManager.BG.toTriple(),
            underline = true
        )

        val SMALL_PRIMARY_TEXT_AND_BORDER =
            YButtonInfo(
                textSize = SizeManager.TEXT_12,
                height = dp32,
                textColor = ColorManager.PRIMARY,
                backgroundColor = ColorManager.TRANSPARENT.toTriple(),
                borderInfo = createBorderInfo(ColorManager.PRIMARY)
            )

        private fun createBorderInfo(color: ColorGetter) =
            BorderInfo(
                color = color,
                alpha = 1f,
                width = dp1
            )

    }

    val rippleDrawInfo = RippleDrawInfo(
        rippleInfo = ColorManager.RIPPLE_INFO,
        backgroundColor = ColorGetter.TRANSPARENT,
        color = textColor
    )

}