package com.sibedge.yokodzun.android.ui.view.circle

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.context_getters.dp_px.dp2
import kotlin.math.min


class CircleBorderDrawable(
    context: Context
) : Drawable() {

    companion object {

        private val BORDER_WIDTH = dp2

    }

    private val borderWidth = BORDER_WIDTH.getPxInt(context).toFloat()

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ColorManager.PRIMARY.get(context)
        style = Paint.Style.STROKE
        strokeWidth = borderWidth
    }

    private var cx = 0f
    private var cy = 0f
    private var radius = 0f

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(cx, cy, radius, borderPaint)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)

        val width = (right - left).toFloat()
        val height = (bottom - top).toFloat()

        cx = left + width / 2
        cy = top + height / 2
        radius = ((min(width, height) - borderWidth) / 2).toInt().toFloat()
    }

    override fun setAlpha(slpha: Int) {
        borderPaint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        borderPaint.colorFilter = colorFilter
    }


}