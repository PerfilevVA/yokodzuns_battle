package com.sibedge.yokodzun.android.ui.button.primary

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.ViewGroup
import com.sibedge.yokodzun.android.ui.gradient.YGradientPaint
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RoundSidesRectCanvasShape
import ru.hnau.androidutils.ui.drawer.Insets
import ru.hnau.androidutils.ui.drawer.ripple.RippleDrawer
import ru.hnau.androidutils.ui.drawer.shadow.drawer.ButtonShadowDrawer
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.touch.TouchHandler
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.toProducer


class PrimaryActionButton(
    context: Context,
    icon: DrawableGetter,
    title: StringGetter,
    onClick: () -> Unit,
    needShowTitle: Producer<Boolean> = true.toProducer(),
    private val info: PrimaryActionButtonInfo = PrimaryActionButtonInfo.DEFAULT
) : ViewGroup(
    context
) {

    private val content =
        PrimaryActivityButtonContent(context, icon, title, needShowTitle, info)

    private val insets: Insets
        get() = info.shadowInfo.insets

    private val boundsProducer = createBoundsProducer()
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
        fromColor = info.backgroundColorFrom,
        toColor = info.backgroundColorTo
    )

    private val rippleDrawer = RippleDrawer(
        animatingView = this,
        rippleDrawInfo = info.rippleDrawInfo,
        animatingViewIsVisibleToUser = isVisibleToUserProducer,
        canvasShape = canvasShape,
        touchHandler = touchHandler
    )

    private val shadowDrawer = ButtonShadowDrawer(
        animatingView = this,
        animatingViewIsVisibleToUser = isVisibleToUserProducer,
        touchHandler = touchHandler,
        canvasShape = canvasShape,
        shadowInfo = info.shadowInfo
    )

    init {
        setSoftwareRendering()
        addView(content)
        boundsProducer.attach { bounds ->
            backgroundPaint.setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        shadowDrawer.draw(canvas)
        canvasShape.draw(canvas, backgroundPaint)
        rippleDrawer.draw(canvas)
        super.dispatchDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        touchHandler.handle(event)
        return true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        content.layout(
            paddingLeft + insets.left.getPxInt(context),
            paddingTop + insets.top.getPxInt(context),
            width - paddingRight - insets.right.getPxInt(context),
            height - paddingBottom - insets.bottom.getPxInt(context)
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val horizontalInsets = horizontalPaddingSum + insets.horizontalSum.getPxInt(context)
        val verticalInsets = verticalPaddingSum + insets.verticalSum.getPxInt(context)
        content.measure(
            resizeMeasureSpec(widthMeasureSpec, -horizontalInsets),
            resizeMeasureSpec(heightMeasureSpec, -verticalInsets)
        )
        setMeasuredDimension(
            content.measuredWidth + horizontalInsets,
            content.measuredHeight + verticalInsets
        )

    }

}

fun <G : ViewGroup> G.addPrimaryActionButton(
    icon: DrawableGetter,
    title: StringGetter,
    onClick: () -> Unit,
    needShowTitle: Producer<Boolean> = true.toProducer(),
    info: PrimaryActionButtonInfo = PrimaryActionButtonInfo.DEFAULT,
    viewConfigurator: (PrimaryActionButton.() -> Unit)? = null
) =
    addChild(
        PrimaryActionButton(context, icon, title, onClick, needShowTitle, info),
        viewConfigurator
    )