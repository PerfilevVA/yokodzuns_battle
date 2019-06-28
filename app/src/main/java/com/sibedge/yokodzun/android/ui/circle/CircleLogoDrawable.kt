package com.sibedge.yokodzun.android.ui.circle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.sibedge.yokodzun.android.R
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutDrawable
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType


class CircleLogoDrawable(
    context: Context
) : Drawable() {

    private val borderDrawable = CircleBorderDrawable(context)

    private val logoDrawable = LayoutDrawable(
        context = context,
        initialLayoutType = LayoutType.Inside,
        initialContent = DrawableGetter(R.drawable.ic_logo)
    )

    override fun draw(canvas: Canvas) {
        borderDrawable.draw(canvas)
        logoDrawable.draw(canvas)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        borderDrawable.setBounds(left, top, right, bottom)
        logoDrawable.setBounds(left, top, right, bottom)
    }

    override fun setAlpha(slpha: Int) {
        logoDrawable.alpha = alpha
        borderDrawable.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        logoDrawable.colorFilter = colorFilter
        borderDrawable.colorFilter = colorFilter
    }

}