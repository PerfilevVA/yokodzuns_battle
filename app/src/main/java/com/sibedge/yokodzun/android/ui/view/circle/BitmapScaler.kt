package com.sibedge.yokodzun.android.ui.view.circle

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import ru.hnau.androidutils.ui.utils.types_utils.doInState
import ru.hnau.androidutils.utils.ReconfigurableBitmapContainer
import ru.hnau.jutils.takeIfPositive


class BitmapScaler(
    private val source: Bitmap
) {

    private val bitmapsGetter = ReconfigurableBitmapContainer()
    private var cachedBitmap: Bitmap? = null
    private val paint = Paint()

    fun get(size: Int): Bitmap? {
        var result = cachedBitmap?.takeIf { it.width == size }
        if (result == null) {
            result = scaleSource(size) ?: return null
            cachedBitmap = result
        }
        return result
    }

    private fun scaleSource(toSize: Int): Bitmap? {

        val result = bitmapsGetter.getBitmap(toSize, toSize, source.config) ?: return null

        val sourceWidth = source.width.takeIfPositive()?.toFloat() ?: return null
        val sourceHeight = source.height.takeIfPositive()?.toFloat() ?: return null

        val resultSize = toSize.takeIfPositive()?.toFloat() ?: return null

        val sourceAspectRatio = sourceWidth / sourceHeight
        val resultAspectRatio = 1f

        val scale: Float
        val x: Float
        val y: Float

        if (sourceAspectRatio > resultAspectRatio) {
            scale = resultSize / sourceHeight
            x = (resultSize / scale - sourceWidth) / 2
            y = 0f
        } else {
            scale = resultSize / sourceWidth
            y = (resultSize / scale - sourceHeight) / 2
            x = 0f
        }

        Canvas(result).doInState {
            scale(scale, scale)
            drawBitmap(source, x, y, paint)
        }

        return result
    }

}