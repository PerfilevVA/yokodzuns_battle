package com.sibedge.yokodzun.android.ui.view.button

import android.content.Context
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter.Companion.dp
import ru.hnau.androidutils.ui.view.clickable.ClickableLayoutDrawableView
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.jutils.handle


class AdditionalButton<T>(
    context: Context,
    private val additionalButtonInfo: (T) -> Info?
) : ClickableLayoutDrawableView(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO,
    initialContent = DrawableGetter.EMPTY
), ViewWithData<T> {

    data class Info(
        val icon: DrawableGetter,
        val action: () -> Unit
    )

    companion object {

        private val PREFERRED_WIDTH = dp(44)

    }

    override val view = this

    private var info: Info? = null
        set(value) {
            field = value
            value.handle(
                ifNotNull = {
                    setVisible()
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