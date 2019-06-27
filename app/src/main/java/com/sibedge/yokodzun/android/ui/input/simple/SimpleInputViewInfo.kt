package com.sibedge.yokodzun.android.ui.input.simple

import android.text.InputType
import android.text.method.TransformationMethod
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.dp_px.*
import ru.hnau.androidutils.ui.drawer.border.BorderInfo
import ru.hnau.androidutils.ui.font_type.FontTypeGetter
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


data class SimpleInputViewInfo(
    val textSize: DpPxGetter = SizeManager.TEXT_16,
    val textColor: ColorGetter = ColorManager.FG,
    val paddingHorizontal: DpPxGetter = SizeManager.LARGE_SEPARATION,
    val paddingVertical: DpPxGetter = SizeManager.DEFAULT_SEPARATION,
    val maxLength: Int? = null,
    val border: BorderInfo = BorderInfo(width = dp1, color = textColor, alpha = 1f),
    val borderMargin: DpPxGetter = dp8,
    val font: FontTypeGetter = FontManager.DEFAULT,
    val hintTextColor: ColorGetter = textColor.mapWithAlpha(0.5f),
    val inputType: Int = InputType.TYPE_CLASS_TEXT,
    val transformationMethod: TransformationMethod? = null
) {

    companion object {

        val DEFAULT = SimpleInputViewInfo()

    }

}