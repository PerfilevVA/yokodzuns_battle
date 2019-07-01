package com.sibedge.yokodzun.android.ui.view

import android.content.Context
import android.graphics.Canvas
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.gradient.YGradientPaint
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RoundSidesRectCanvasShape
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.getMaxMeasurement
import ru.hnau.androidutils.ui.view.utils.makeMeasureSpec
import ru.hnau.jutils.handle
import ru.hnau.jutils.ifTrue
import kotlin.math.min


class ChipLabel(
    context: Context,
    info: LabelInfo
) : Label(
    context = context,
    info = info
),
    ViewWithContent<ChipLabel.Info> {

    data class Info(
        val text: StringGetter,
        val color: ColorTriple
    )

    override val view = this

    private val backgroundPaint = YGradientPaint(context)

    override var data: Info? = null
        set(value) {
            field = value
            text = value?.text ?: StringGetter.EMPTY
            backgroundPaint.color = value?.color ?: ColorManager.PRIMARY_TRIPLE
            applyHorizontalPadding(
                (text.get(context).length > 1).handle(
                    forTrue = SizeManager.SMALL_SEPARATION,
                    forFalse = SizeManager.EXTRA_SMALL_SEPARATION
                )
            )
        }

    private val boundsProducer = createBoundsProducer(false)
    private val canvasShape = RoundSidesRectCanvasShape(boundsProducer)

    init {
        boundsProducer.attach {
            backgroundPaint.setBounds(it.left, it.top, it.right, it.bottom)
        }
        applyPadding(SizeManager.EXTRA_SMALL_SEPARATION)
    }

    override fun draw(canvas: Canvas) {
        canvasShape.draw(canvas, backgroundPaint)
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