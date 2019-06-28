package com.sibedge.yokodzun.android.ui.circle

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import ru.hnau.androidutils.context_getters.dp_px.dp2
import ru.hnau.androidutils.context_getters.dp_px.dp32
import kotlin.math.min


class CircleLetterDrawable(
    private val context: Context,
    text: String
) : Drawable() {

    companion object {

        private val TEXT_SIZE = dp32

    }

    private val borderDrawable = CircleBorderDrawable(context)

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ColorManager.PRIMARY.get(context)
        textSize = TEXT_SIZE.getPxInt(context).toFloat()
        typeface = FontManager.DEFAULT.get(context).typeface
        textAlign = Paint.Align.CENTER
    }

    private val text = text.firstOrNull()?.toString() ?: ""


    private var textX = 0f
    private var textY = 0f

    override fun draw(canvas: Canvas) {
        borderDrawable.draw(canvas)
        canvas.drawText(text, textX, textY, textPaint)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        borderDrawable.setBounds(left, top, right, bottom)

        val width = (right - left).toFloat()
        val height = (bottom - top).toFloat()

        val cx = left + width / 2
        val cy = top + height / 2

        textX = cx
        textY = cy + TEXT_SIZE.getPx(context) / 2 - textPaint.fontMetrics.descent
    }

    override fun setAlpha(slpha: Int) {
        textPaint.alpha = alpha
        borderDrawable.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        textPaint.colorFilter = colorFilter
        borderDrawable.colorFilter = colorFilter
    }

}