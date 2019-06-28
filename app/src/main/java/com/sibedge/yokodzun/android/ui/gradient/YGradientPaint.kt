package com.sibedge.yokodzun.android.ui.gradient

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.utils.ContextConnector.context


class YGradientPaint(
    private val context: Context,
    private val fromColor: ColorGetter = ColorManager.PRIMARY_LIGHT,
    private val toColor: ColorGetter = ColorManager.PRIMARY_DARK

) : Paint(ANTI_ALIAS_FLAG) {

    fun setBounds(left: Number, top: Number, right: Number, bottom: Number) {
        shader = LinearGradient(
            left.toFloat(), top.toFloat(),
            right.toFloat(), bottom.toFloat(),
            fromColor.get(context),
            toColor.get(context),
            Shader.TileMode.CLAMP
        )
    }

}