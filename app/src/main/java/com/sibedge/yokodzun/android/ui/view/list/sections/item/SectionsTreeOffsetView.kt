package com.sibedge.yokodzun.android.ui.view.list.sections.item

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.ui.view.list.sections.SectionsTreeUtils
import ru.hnau.androidutils.context_getters.dp_px.dp24
import ru.hnau.androidutils.context_getters.dp_px.dp4
import ru.hnau.androidutils.ui.view.utils.getDefaultMeasurement


class SectionsTreeOffsetView(
    context: Context
) : View(
    context
), ViewWithData<Int> {

    companion object {

        private val ITEM_WIDTH = dp24
        private val CIRCLE_RADIUS = dp4

    }

    private val itemWidth = ITEM_WIDTH.getPxInt(context)
    private val circleRadius = CIRCLE_RADIUS.getPxInt(context).toFloat()
    private val circleSize = (circleRadius * 2 + 2).toInt()

    private val circlesPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override val view = this

    override var data: Int? = null
        set(value) {
            field = value
            requestLayout()
        }

    private val offsetCount get() = data ?: 0

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val cy = height / 2f
        repeat(offsetCount) { i ->
            val x = (i + 1f) * itemWidth - circleRadius
            circlesPaint.color = SectionsTreeUtils.getOffsetColor(i).main.get(context)
            canvas.drawCircle(x, cy, circleRadius, circlesPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getDefaultMeasurement(widthMeasureSpec, itemWidth * offsetCount),
            getDefaultMeasurement(heightMeasureSpec, circleSize)
        )
    }


}