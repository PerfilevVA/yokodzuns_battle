package com.sibedge.yokodzun.android.ui.gradient

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.context_getters.ColorGetter


class YGradientDrawable(
    context: Context,
    color: ColorTriple = ColorManager.PRIMARY_TRIPLE
) : Drawable() {

    private val paint = YGradientPaint(context, color)

    override fun draw(canvas: Canvas) =
        canvas.drawRect(bounds, paint)

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.OPAQUE

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        paint.setBounds(left, top, right, bottom)
    }

}