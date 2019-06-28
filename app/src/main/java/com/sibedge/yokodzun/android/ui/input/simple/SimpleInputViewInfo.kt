package com.sibedge.yokodzun.android.ui.input.simple

import android.text.InputType
import android.text.method.TransformationMethod
import android.view.Gravity
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.dp_px.*
import ru.hnau.androidutils.ui.drawer.border.BorderInfo
import ru.hnau.androidutils.ui.font_type.FontTypeGetter
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity


data class SimpleInputViewInfo(
    val textSize: DpPxGetter = SizeManager.TEXT_16,
    val textColor: ColorGetter = ColorManager.FG,
    val paddingHorizontal: DpPxGetter = SizeManager.LARGE_SEPARATION,
    val paddingVertical: DpPxGetter = SizeManager.DEFAULT_SEPARATION,
    val maxLength: Int? = null,
    val gravity: HGravity = HGravity.START_CENTER_VERTICAL,
    val border: BorderInfo = BorderInfo(width = dp2, color = textColor, alpha = 1f),
    val borderMargin: DpPxGetter = dp8,
    val font: FontTypeGetter = FontManager.DEFAULT,
    val hintTextColor: ColorGetter = textColor.mapWithAlpha(0.5f),
    val inputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
    val transformationMethod: TransformationMethod? = null
) {

    companion object {

        val DEFAULT = SimpleInputViewInfo()

    }

}