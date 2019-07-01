package com.sibedge.yokodzun.android.ui.view

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RoundSidesRectCanvasShape
import ru.hnau.androidutils.ui.drawer.ripple.RippleDrawer
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.utils.setInvisible
import ru.hnau.androidutils.ui.view.utils.setVisible
import ru.hnau.androidutils.ui.view.utils.touch.TouchHandler
import ru.hnau.jutils.handle


class CountView(
    context: Context,
    title: StringGetter,
    private val color: ColorTriple,
    onClick: () -> Unit
) : LinearLayout(
    context
), ViewWithContent<Int> {

    companion object {

        private val TEXT_SIZE = SizeManager.TEXT_12

    }

    override val view = this

    private val titleView = Label(
        context = context,
        textSize = TEXT_SIZE,
        textColor = color.main,
        fontType = FontManager.DEFAULT,
        gravity = HGravity.START_CENTER_VERTICAL,
        maxLines = 1,
        minLines = 1,
        initialText = title,
        ellipsize = false
    ).applyLinearParams {
        setStretchedWidth()
        setEndMargin(SizeManager.EXTRA_SMALL_SEPARATION)
    }

    private val numberView = ChipLabel(
        context = context,
        info = LabelInfo(
            textSize = TEXT_SIZE,
            textColor = ColorManager.FG,
            fontType = FontManager.BOLD,
            gravity = HGravity.CENTER,
            maxLines = 1,
            minLines = 1
        )
    )

    override var data: Int? = null
        set(value) {
            field = value
            value.handle(
                ifNotNull = {
                    numberView.setVisible()
                    numberView.data = ChipLabel.Info(
                        text = it.toString().toGetter(),
                        color = color
                    )
                },
                ifNull = {
                    numberView.setInvisible()
                }
            )
        }

    private val boundsProducer = createBoundsProducer(false)
    private val isVisibleToUserProducer = createIsVisibleToUserProducer()
    private val canvasShape = RoundSidesRectCanvasShape(boundsProducer)
    private val touchHandler = TouchHandler(
        canvasShape = canvasShape,
        onClick = onClick
    )
    private val rippleDrawer = RippleDrawer(
        canvasShape = canvasShape,
        animatingView = this,
        animatingViewIsVisibleToUser = isVisibleToUserProducer,
        touchHandler = touchHandler,
        rippleDrawInfo = RippleDrawInfo(
            rippleInfo = ColorManager.RIPPLE_INFO,
            backgroundColor = ColorManager.TRANSPARENT,
            color = color.main,
            rippleAlpha = ColorManager.RIPPLE_ALPHA
        )
    )

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        addView(titleView)
        addView(numberView)
    }

    override fun dispatchDraw(canvas: Canvas) {
        rippleDrawer.draw(canvas)
        super.dispatchDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        touchHandler.handle(event)
        return true
    }


}