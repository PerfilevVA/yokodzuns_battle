package com.sibedge.yokodzun.android.ui.action_code

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RoundSidesRectCanvasShape
import ru.hnau.androidutils.ui.drawer.ripple.RippleDrawer
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.utils.touch.TouchHandler
import ru.hnau.androidutils.utils.shortToast
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.android.utils.Utils


@SuppressLint("ViewConstructor")
class ActionCodeView(
    context: Context,
    private val actionCode: String
) : Label(
    context = context,
    info = LabelInfo(
        gravity = HGravity.CENTER,
        fontType = FontManager.UBUNTU_MONO,
        minLines = 1,
        maxLines = 1,
        textColor = ColorManager.PRIMARY,
        textSize = SizeManager.TEXT_20
    ),
    initialText = formatCode(actionCode).toGetter()
) {

    companion object {

        private val CLIPBOARD_LABEL = StringGetter(R.string.action_code_copy_label)

        private fun formatCode(code: String) =
            code.map { it }.joinToString(" ")

    }

    private val boundsProducer = createBoundsProducer(
        usePaddings = false
    )

    private val canvasShape = RoundSidesRectCanvasShape(boundsProducer)

    private val touchHandler = TouchHandler(
        canvasShape = canvasShape,
        onClick = {
            Utils.copyTextToClipboard(
                label = CLIPBOARD_LABEL.get(context),
                text = actionCode
            )
            shortToast(StringGetter(R.string.action_code_copy_success))
        }
    )

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val rippleDrawer = RippleDrawer(
        animatingView = this,
        animatingViewIsVisibleToUser = isVisibleToUserProducer,
        canvasShape = canvasShape,
        touchHandler = touchHandler,
        rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
    )

    override fun draw(canvas: Canvas) {
        rippleDrawer.draw(canvas)
        super.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchHandler.handle(event)
        return true
    }

}