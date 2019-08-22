package com.sibedge.yokodzun.android.layers.rates

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.utils.RateUtils
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.context_getters.dp_px.dp1
import ru.hnau.androidutils.context_getters.dp_px.dp24
import ru.hnau.androidutils.ui.utils.types_utils.initAsRoundSideRect
import ru.hnau.androidutils.ui.view.utils.getDefaultMeasurement
import ru.hnau.androidutils.ui.view.utils.getMaxMeasurement
import ru.hnau.androidutils.ui.view.utils.verticalPaddingSum
import ru.hnau.jutils.getFloatInterFloats


class TeamRateValueView(
    context: Context
) : View(
    context
), ViewWithData<Float> {

    companion object {

        private val BORDER_WIDTH = dp1
        private val PREFERRED_HEIGHT = dp24

    }

    override val view = this

    private val memSaveRect = RectF()

    private val boundsRect = RectF()
    private val valueRect = RectF()

    private val borderWidth = BORDER_WIDTH.getPxInt(context)
    private val borderInsets = borderWidth / 2

    private val borderPath = Path()
    private val valuePath = Path()

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = borderWidth.toFloat()
    }

    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override var data: Float? = null
        set(value) {
            field = value
            recalculateValuePath()
            val color = (value?.let(RateUtils::getValueColor) ?: ColorManager.FG_T50).get(context)
            borderPaint.color = color
            valuePaint.color = color
            invalidate()
        }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawPath(valuePath, valuePaint)
        canvas.drawPath(borderPath, borderPaint)
    }

    private fun recalculateValuePath() {

        val value = data
        if (value == null) {
            valuePath.reset()
            return
        }

        val minWidth = boundsRect.height()
        val maxWidth = boundsRect.width()
        val width = getFloatInterFloats(minWidth, maxWidth, value)
        valueRect.set(
            boundsRect.left,
            boundsRect.top,
            boundsRect.left + width,
            boundsRect.bottom
        )
        valuePath.initAsRoundSideRect(valueRect, memSaveRect)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        boundsRect.set(
            paddingLeft + borderInsets.toFloat(),
            paddingTop + borderInsets.toFloat(),
            width - paddingRight - borderInsets.toFloat(),
            height - paddingBottom - borderInsets.toFloat()
        )
        borderPath.initAsRoundSideRect(boundsRect, memSaveRect)
        recalculateValuePath()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getMaxMeasurement(widthMeasureSpec, 0),
            getDefaultMeasurement(
                heightMeasureSpec,
                PREFERRED_HEIGHT.getPxInt(context) + verticalPaddingSum
            )
        )
    }

}