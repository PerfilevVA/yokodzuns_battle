package com.sibedge.yokodzun.android.ui.gradient

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.utils.ContextConnector.context


class YGradientPaint(
    private val context: Context,
    color: ColorTriple = ColorManager.PRIMARY_TRIPLE
) : Paint(ANTI_ALIAS_FLAG) {

    private val boundsRect = RectF()

    var color: ColorTriple = color
        set(value) {
            if (field != value) {
                field = value
                updateGradient()
            }
        }

    private fun updateGradient() {
        shader = LinearGradient(
            boundsRect.left, boundsRect.top,
            boundsRect.right, boundsRect.bottom,
            intArrayOf(
                color.light.get(context),
                color.main.get(context),
                color.dark.get(context)
            ),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )
    }

    fun setBounds(left: Number, top: Number, right: Number, bottom: Number) {
        boundsRect.set(
            left.toFloat(), top.toFloat(),
            right.toFloat(), bottom.toFloat()
        )
        updateGradient()
    }

}