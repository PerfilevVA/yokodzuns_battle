package com.sibedge.yokodzun.android.ui.cell

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.widget.LinearLayout
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RectCanvasShape
import ru.hnau.androidutils.ui.drawer.ripple.RippleDrawer
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.view.clickable.ClickableLinearLayout
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.utils.touch.TouchHandler
import com.sibedge.yokodzun.android.utils.managers.ColorManager


@SuppressLint("ViewConstructor")
abstract class Cell<T : Any>(
    context: Context,
    rippleDrawInfo: RippleDrawInfo,
    private val onClick: ((T) -> Unit)? = null
) : LinearLayout(
    context
), BaseListViewWrapper<T> {

    override val view = this

    protected var content: T? = null
        private set(value) {
            if (field != value && value != null) {
                field = value
                onContentReceived(value)
            }
        }

    final override fun setContent(content: T, position: Int) {
        this.content = content
    }

    protected abstract fun onContentReceived(content: T)

    private val boundsProducer =
        createBoundsProducer(false)

    private val canvasShape = RectCanvasShape(boundsProducer)

    private val touchHandler = TouchHandler(
        canvasShape = canvasShape,
        onClick = this::onClick
    )

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val rippleDrawer = RippleDrawer(
        animatingView = this,
        animatingViewIsVisibleToUser = isVisibleToUserProducer,
        touchHandler = touchHandler,
        canvasShape = canvasShape,
        rippleDrawInfo = rippleDrawInfo
    )

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        onClick?.let { touchHandler.handle(event) }
        return true
    }

    protected open fun onClick() {
        onClick?.let { content?.let(it) }
    }

    override fun dispatchDraw(canvas: Canvas) {
        rippleDrawer.draw(canvas)
        super.dispatchDraw(canvas)
    }

}