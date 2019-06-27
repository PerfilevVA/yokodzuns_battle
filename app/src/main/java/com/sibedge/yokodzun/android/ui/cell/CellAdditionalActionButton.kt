package com.sibedge.yokodzun.android.ui.cell

import android.content.Context
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.clickable.ClickableLayoutDrawableView
import ru.hnau.androidutils.ui.view.header.Header
import ru.hnau.androidutils.ui.view.utils.getDefaultMeasurement
import ru.hnau.androidutils.ui.view.utils.getMaxMeasurement
import ru.hnau.androidutils.ui.view.utils.setGone
import ru.hnau.androidutils.ui.view.utils.setVisible


class CellAdditionalActionButton(
    context: Context,
    rippleDrawInfo: RippleDrawInfo
) : ClickableLayoutDrawableView(
    context = context,
    initialGravity = HGravity.CENTER,
    initialLayoutType = LayoutType.Independent,
    initialContent = DrawableGetter.EMPTY,
    rippleDrawInfo = rippleDrawInfo
) {

    data class Info(
        val icon: DrawableGetter,
        val onClick: () -> Unit
    )

    companion object {

        private val PREFERRED_WIDTH = Header.DEFAULT_HEIGHT

    }

    var info: Info? = null
        set(value) {
            if (field != value) {
                field = value
                onInfoChanged(value)
            }
        }

    init {
        onInfoChanged(null)
    }

    private fun onInfoChanged(info: Info?) {
        if (info == null) {
            setGone()
            return
        }
        setVisible()
        content = info.icon
    }

    override fun onClick() {
        super.onClick()
        info?.onClick?.invoke()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getDefaultMeasurement(widthMeasureSpec, PREFERRED_WIDTH.getPxInt(context))
        val height = getMaxMeasurement(heightMeasureSpec, 0)
        setMeasuredDimension(width, height)
    }

}