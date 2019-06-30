package com.sibedge.yokodzun.android.ui.view.button

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.sibedge.yokodzun.android.ui.gradient.YGradientPaint
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.bounds_producer.ViewBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RoundSidesRectCanvasShape
import ru.hnau.androidutils.ui.drawer.Insets
import ru.hnau.androidutils.ui.drawer.border.BorderDrawer
import ru.hnau.androidutils.ui.drawer.ripple.RippleDrawer
import ru.hnau.androidutils.ui.drawer.shadow.drawer.ButtonShadowDrawer
import ru.hnau.androidutils.ui.utils.types_utils.doInState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.utils.getDefaultMeasurement
import ru.hnau.androidutils.ui.view.utils.setSoftwareRendering
import ru.hnau.androidutils.ui.view.utils.touch.TouchHandler


@SuppressLint("ViewConstructor")
class YButton(
    context: Context,
    text: StringGetter,
    onClick: () -> Unit,
    private val info: YButtonInfo
) : View(context) {

    private val borderInsets =
        info.borderInfo?.insets ?: Insets.EMPTY

    private val insets =
        (info.shadow?.insets ?: Insets.EMPTY) + borderInsets

    private val boundsProducer = ViewBoundsProducer(
        view = this,
        usePaddings = false
    )
        .applyInsents(context, insets)

    private val canvasShape =
        RoundSidesRectCanvasShape(boundsProducer)

    private val touchHandler = TouchHandler(
        canvasShape = canvasShape,
        onClick = onClick
    )

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val backgroundPaint = YGradientPaint(
        context = context,
        color = info.backgroundColor
    )

    private val rippleDrawer = RippleDrawer(
        animatingView = this,
        animatingViewIsVisibleToUser = isVisibleToUserProducer,
        rippleDrawInfo = info.rippleDrawInfo,
        canvasShape = canvasShape,
        touchHandler = touchHandler
    )

    private val buttonShadowDrawer = info.shadow?.let { shadowInfo ->
        ButtonShadowDrawer(
            animatingView = this,
            animatingViewIsVisibleToUser = isVisibleToUserProducer,
            touchHandler = touchHandler,
            canvasShape = canvasShape,
            shadowInfo = shadowInfo
        )
    }

    private val borderDrawer = info.borderInfo?.let { borderInfo ->
        BorderDrawer(
            context = context,
            borderInfo = borderInfo,
            canvasShape = canvasShape
        )
    }

    private val text = text.get(context).toUpperCase()

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = info.textColor.get(context)
        textSize = info.textSize.getPxInt(context).toFloat()
        typeface = info.font.get(context).typeface
        textAlign = Paint.Align.CENTER
        if (info.underline) {
            flags = flags or Paint.UNDERLINE_TEXT_FLAG
        }
    }

    private val textWidth = textPaint.measureText(this.text)

    private val preferredWidth: Int
        get() = (textWidth + info.paddingHorizontal.getPx(context) + insets.horizontalSum.getPx(
            context
        )).toInt()

    private val preferredHeight: Int
        get() = (info.height.getPx(context) + insets.verticalSum.getPx(context)).toInt()

    private val textDrawPoint = PointF()

    init {
        setSoftwareRendering()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        buttonShadowDrawer?.draw(canvas)
        canvasShape.draw(canvas, backgroundPaint)
        rippleDrawer.draw(canvas)
        canvas.doInState {
            canvasShape.clip(canvas)
            drawText(text, textDrawPoint.x, textDrawPoint.y, textPaint)
        }
        borderDrawer?.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchHandler.handle(event)
        return true
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val cx = (insets.left.getPx(context) + width - insets.right.getPx(context)) / 2f
        val cy = (insets.top.getPx(context) + height - insets.bottom.getPx(context)) / 2f
        textDrawPoint.set(
            cx,
            cy + (-textPaint.fontMetrics.ascent - textPaint.fontMetrics.descent) / 2
        )
        backgroundPaint.setBounds(
            left = insets.left.getPx(context),
            top = insets.top.getPx(context),
            right = width - insets.right.getPx(context),
            bottom = height - insets.bottom.getPx(context)
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getDefaultMeasurement(widthMeasureSpec, preferredWidth),
            getDefaultMeasurement(heightMeasureSpec, preferredHeight)
        )
    }

}

fun <G : ViewGroup> G.addYButton(
    text: StringGetter,
    onClick: () -> Unit,
    info: YButtonInfo,
    viewConfigurator: (YButton.() -> Unit)? = null
) = addChild(
    YButton(context, text, onClick, info),
    viewConfigurator
)