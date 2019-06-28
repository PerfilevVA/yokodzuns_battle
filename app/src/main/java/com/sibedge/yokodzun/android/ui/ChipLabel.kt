package com.sibedge.yokodzun.android.ui

import android.content.Context
import android.graphics.Canvas
import android.view.View
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.gradient.YGradientPaint
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RoundSidesRectCanvasShape
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.getMaxMeasurement
import ru.hnau.androidutils.ui.view.utils.makeMeasureSpec
import ru.hnau.jutils.ifTrue
import kotlin.math.min


class ChipLabel(
    context: Context,
    info: LabelInfo,
    chipColorFrom: ColorGetter,
    chipColorTo: ColorGetter
) : Label(
    context = context,
    info = info
), ViewWithContent<StringGetter> {

    override val view = this

    override var content: StringGetter? = null
        set(value) {
            field = value
            text = value ?: StringGetter.EMPTY
        }

    private val boundsProducer = createBoundsProducer(false)
    private val canvasShape = RoundSidesRectCanvasShape(boundsProducer)

    private val backgrounbdPaint = YGradientPaint(
        context = context,
        fromColor = chipColorFrom,
        toColor = chipColorTo
    )

    init {
        boundsProducer.attach {
            backgrounbdPaint.setBounds(it.left, it.top, it.right, it.bottom)
        }
        applyPadding(SizeManager.EXTRA_SMALL_SEPARATION)
    }

    override fun draw(canvas: Canvas) {
        canvasShape.draw(canvas, backgrounbdPaint)
        super.draw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        val width = measuredWidth
        (width >= height).ifTrue { return }
        val maxWidth = getMaxMeasurement(widthMeasureSpec, 0)
        val newWidth = min(maxWidth, height).takeIf { it != width } ?: return
        super.onMeasure(makeMeasureSpec(newWidth, MeasureSpec.EXACTLY), heightMeasureSpec)
    }

}