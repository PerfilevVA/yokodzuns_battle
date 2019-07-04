package com.sibedge.yokodzun.android.ui.view.button

import android.content.Context
import android.graphics.Canvas
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.ui.gradient.YGradientDrawable
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.Utils
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.dp_px.dp64
import ru.hnau.androidutils.ui.view.clickable.ClickableLayoutDrawableView
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.jutils.handle


class AdditionalButton<T>(
    context: Context,
    private val additionalButtonInfo: (T) -> Info?
) : ClickableLayoutDrawableView(
    context = context,
    rippleDrawInfo = ColorManager.FG_ON_TRANSPARENT_RIPPLE_INFO,
    initialContent = DrawableGetter.EMPTY
), ViewWithData<T> {

    data class Info(
        val icon: DrawableGetter,
        val color: ColorTriple,
        val action: () -> Unit
    )

    companion object {

        private val PREFERRED_WIDTH = Utils.HEADER_HEIGHT

    }

    override val view = this

    private val background = YGradientDrawable(context)

    private var info: Info? = null
        set(value) {
            field = value
            value.handle(
                ifNotNull = {
                    setVisible()
                    background.color = it.color
                    content = it.icon
                },
                ifNull = {
                    setInvisible()
                }
            )
        }

    override var data: T? = null
        set(value) {
            field = value
            info = value?.let(additionalButtonInfo)
        }

    init {
        setInvisible()
    }

    override fun draw(canvas: Canvas) {
        //TODO Uncomment for color background
        //background.draw(canvas)
        super.draw(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        background.setBounds(0, 0, width, height)
    }

    override fun onClick() {
        super.onClick()
        info?.action?.invoke()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            makeExactlyMeasureSpec(
                getDefaultMeasurement(widthMeasureSpec, PREFERRED_WIDTH.getPxInt(context))
            ),
            heightMeasureSpec
        )
    }

}