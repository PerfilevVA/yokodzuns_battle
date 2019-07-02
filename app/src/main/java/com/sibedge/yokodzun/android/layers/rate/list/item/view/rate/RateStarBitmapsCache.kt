package com.sibedge.yokodzun.android.layers.rate.list.item.view.rate

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.drawable.DrawableCompat
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.RateUtils
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.dp_px.dp24
import ru.hnau.androidutils.context_getters.dp_px.dp40
import ru.hnau.androidutils.utils.ContextConnector
import ru.hnau.jutils.cache.AutoCache
import ru.hnau.jutils.getFloatInterFloats
import ru.hnau.jutils.handle


object RateStarBitmapsCache {

    private val MIN_SIZE = dp24
    private val MAX_SIZE = dp40

    private val DRAWABLES: Map<Boolean, DrawableGetter> = mapOf(
        false to DrawableGetter(R.drawable.ic_rate_empty_fg),
        true to DrawableGetter(R.drawable.ic_rate_full_fg)
    )

    private data class Key(
        val mark: Int,
        val selected: Boolean
    )

    private val cache = AutoCache<Key, Bitmap>(
        capacity = RateUtils.MARKS_COUNT * 2,
        getter = this::generateBitmap
    )

    fun get(mark: Int, selected: Boolean) =
        cache.get(
            Key(
                mark,
                selected
            )
        )

    private fun generateBitmap(key: Key): Bitmap {
        val (mark, selected) = key
        val context = ContextConnector.context
        val value = RateUtils.markToValue(mark.toFloat())
        val minSize = MIN_SIZE.getPx(context)
        val maxSize = MAX_SIZE.getPx(context)
        val size = getFloatInterFloats(minSize, maxSize, value).toInt()
        val drawable = DRAWABLES.getValue(selected).get(context)
        val color = selected.handle(
            onTrue = { RateUtils.getValueColor(value) },
            onFalse = { ColorManager.FG_T50 }
        )
        DrawableCompat.setTint(drawable, color.get(context))
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        drawable.setBounds(0, 0, size, size)
        drawable.draw(Canvas(bitmap))
        return bitmap
    }

}