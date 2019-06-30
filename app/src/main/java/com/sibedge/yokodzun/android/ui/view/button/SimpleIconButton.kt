package com.sibedge.yokodzun.android.ui.view.button

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.CircleCanvasShape
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType
import ru.hnau.androidutils.ui.drawables.layout_drawable.view.LayoutDrawableView
import ru.hnau.androidutils.ui.drawer.ripple.RippleDrawer
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.touch.TouchHandler
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import kotlin.math.min


open class SimpleIconButton(
    context: Context,
    icon: DrawableGetter,
    rippleDrawInfo: RippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO,
    private val onClick: (() -> Unit)? = null
) : LayoutDrawableView(
    context = context,
    initialContent = icon
) {

    private val boundsProducer =
        createBoundsProducer()

    private val canvasShape =
        CircleCanvasShape(boundsProducer)

    private val touchHandler = TouchHandler(
        canvasShape = canvasShape,
        onClick = this::onClick
    )

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val rippleDrawer = RippleDrawer(
        animatingView = this,
        animatingViewIsVisibleToUser = isVisibleToUserProducer,
        canvasShape = canvasShape,
        touchHandler = touchHandler,
        rippleDrawInfo = rippleDrawInfo
    )

    override fun onDraw(canvas: Canvas) {
        rippleDrawer.draw(canvas)
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        touchHandler.handle(event)
        return true
    }

    protected open fun onClick() {
        onClick?.invoke()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = getMaxMeasurement(widthMeasureSpec, Int.MAX_VALUE) - horizontalPaddingSum
        val maxHeight = getMaxMeasurement(heightMeasureSpec, Int.MAX_VALUE) - verticalPaddingSum
        val preferredSize = min(maxWidth, maxHeight)
        setMeasuredDimension(
            getDefaultMeasurement(widthMeasureSpec, preferredSize + horizontalPaddingSum),
            getDefaultMeasurement(heightMeasureSpec, preferredSize + verticalPaddingSum)
        )
    }


}