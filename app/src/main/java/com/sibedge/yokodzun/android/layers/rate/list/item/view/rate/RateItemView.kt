package com.sibedge.yokodzun.android.layers.rate.list.item.view.rate

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.sibedge.yokodzun.android.utils.RateUtils
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.CircleCanvasShape
import ru.hnau.androidutils.ui.drawer.ripple.RippleDrawer
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.utils.touch.TouchHandler
import ru.hnau.jutils.handle
import ru.hnau.jutils.ifTrue


class RateItemView(
    context: Context,
    mark: Int,
    onMarkClick: (Int) -> Unit,
    private val selected: Boolean,
    private val touchable: Boolean
) : View(context) {

    private val bitmap by lazy {
        RateStarBitmapsCache.get(mark, selected, touchable)
    }
    private val paint = Paint()

    private val isVisibleToUserProducer = createIsVisibleToUserProducer()
    private val boundsProducer = createBoundsProducer()
    private val canvasShape = CircleCanvasShape(boundsProducer)
    private val touchHandler = TouchHandler(
        canvasShape = canvasShape,
        onClick = { onMarkClick(mark) }
    )
    private val rippleDrawer = RippleDrawer(
        animatingView = this,
        animatingViewIsVisibleToUser = isVisibleToUserProducer,
        canvasShape = canvasShape,
        touchHandler = touchHandler,
        rippleDrawInfo = RippleDrawInfo(
            rippleInfo = ColorManager.RIPPLE_INFO,
            backgroundColor = ColorManager.TRANSPARENT,
            color = touchable.handle(
                onTrue = { RateUtils.getMarkColor(mark.toFloat()) },
                onFalse = { RateUtils.getUntouchableMarkColor(mark.toFloat()) }
            ),
            rippleAlpha = ColorManager.RIPPLE_ALPHA
        )
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rippleDrawer.draw(canvas)
        val left = (width - bitmap.width) / 2f
        val top = (height - bitmap.height) / 2f
        canvas.drawBitmap(bitmap, left, top, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchable.ifTrue {
            super.onTouchEvent(event)
            touchHandler.handle(event)
        }
        return true
    }
}