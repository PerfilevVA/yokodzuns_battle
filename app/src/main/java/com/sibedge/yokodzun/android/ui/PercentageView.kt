package com.sibedge.yokodzun.android.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import ru.hnau.androidutils.animations.smooth
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.dp_px.dp16
import ru.hnau.androidutils.context_getters.dp_px.dp2
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RoundSidesRectCanvasShape
import ru.hnau.androidutils.ui.drawer.border.BorderDrawer
import ru.hnau.androidutils.ui.drawer.border.BorderInfo
import ru.hnau.androidutils.ui.utils.types_utils.initAsRoundSideRect
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.getFloatInterFloats
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.coerceIn
import ru.hnau.jutils.producer.extensions.observeWhen


class PercentageView(
    context: Context,
    progress: Producer<Float>,
    color: ColorGetter
) : View(context) {

    companion object {

        private val PREFERRED_HEIGHT = dp16

    }

    private val borderInfo = BorderInfo(
        width = dp2,
        color = color
    )

    private val boundsProducer = createBoundsProducer()
        .applyInsents(context, borderInfo.insets)

    private val canvasShape = RoundSidesRectCanvasShape(
        boundsProducer = boundsProducer
    )

    private val borderDrawer = BorderDrawer(
        borderInfo = borderInfo,
        context = context,
        canvasShape = canvasShape
    )

    private var bounds = RectF()
        set(value) {
            field = value
            updateProgressPath()
        }

    private var progress = 0f
        set(value) {
            field = value
            updateProgressPath()
        }

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val memSaveRect = RectF()
    private val backgroundBounds = RectF()
    private val backgroundPath = Path()
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color.get(context)
    }

    init {
        progress
            .coerceIn(0f, 1f)
            .smooth(TimeValue.SECOND)
            .observeWhen(isVisibleToUserProducer) {
                this.progress = it
            }

        boundsProducer
            .observeWhen(isVisibleToUserProducer) {
                bounds = it
            }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.drawPath(backgroundPath, backgroundPaint)
        borderDrawer.draw(canvas)
    }

    private fun updateProgressPath() {
        val maxWidth = bounds.width()
        val minWidth = bounds.height()
        val width = getFloatInterFloats(minWidth, maxWidth, progress)
        backgroundBounds.set(
            bounds.left,
            bounds.top,
            bounds.left + width,
            bounds.bottom
        )
        backgroundPath.initAsRoundSideRect(backgroundBounds, memSaveRect)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getMaxMeasurement(widthMeasureSpec, 0)
        val height = getDefaultMeasurement(heightMeasureSpec, PREFERRED_HEIGHT.getPxInt(context) - verticalPaddingSum)
        setMeasuredDimension(width, height)
    }


}