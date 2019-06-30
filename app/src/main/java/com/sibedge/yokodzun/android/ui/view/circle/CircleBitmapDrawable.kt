package com.sibedge.yokodzun.android.ui.view.circle

import android.graphics.*
import android.graphics.drawable.Drawable
import ru.hnau.androidutils.ui.utils.types_utils.doInState
import kotlin.math.min


class CircleBitmapDrawable(
    source: Bitmap
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val scaler = BitmapScaler(source)

    private var dx = 0f
    private var dy = 0f
    private var radius = 0f

    private val preferredSize = min(source.width, source.height)

    override fun getIntrinsicWidth() = preferredSize
    override fun getIntrinsicHeight() = preferredSize

    override fun draw(
        canvas: Canvas
    ) {
        paint.shader ?: return
        canvas.doInState {
            translate(dx, dy)
            drawCircle(radius, radius, radius, paint)
        }
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        layout(left, top, right, bottom)
    }

    private fun layout(
        left: Int, top: Int,
        right: Int, bottom: Int
    ) {
        val width = right - left
        val height = bottom - top

        val size = min(width, height)

        val bitmap = scaler.get(size)
        paint.shader = bitmap?.let {
            BitmapShader(it, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        }

        radius = size / 2f
        dx = -(left + (width - size) / 2f)
        dy = -(top + (height - size) / 2f)

    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}