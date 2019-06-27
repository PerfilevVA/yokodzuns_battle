package com.sibedge.yokodzun.android.ui

import android.content.Context
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.drawables.layout_drawable.view.LayoutDrawableView
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.clickable.ClickableLinearLayout
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class RTCheckBox(
    context: Context,
    val index: Int,
    checked: Boolean,
    text: String
) : ClickableLinearLayout(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
) {

    companion object {

        private val ICON_CHECKED = DrawableGetter(R.drawable.ic_check_box_on)
        private val ICON_NOT_CHECKED = DrawableGetter(R.drawable.ic_check_box_off)

    }

    var checked = !checked
        private set(value) {
            if (field != value) {
                field = value
                iconView.content = if (value) ICON_CHECKED else ICON_NOT_CHECKED
            }
        }

    private val iconView = LayoutDrawableView(
        context = context,
        initialContent = ICON_NOT_CHECKED
    )

    private val labelView = Label(
        context = context,
        maxLines = 1,
        minLines = 1,
        initialText = text.toGetter(),
        textSize = SizeManager.TEXT_12,
        gravity = HGravity.START_CENTER_VERTICAL,
        textColor = ColorManager.FG
    ).applyLinearParams {
        setStretchedWidth()
        setStartMargin(SizeManager.DEFAULT_SEPARATION)
    }

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        applyHorizontalPadding(SizeManager.LARGE_SEPARATION)
        applyVerticalPadding(SizeManager.SMALL_SEPARATION)

        addChild(iconView)
        addChild(labelView)

        this.checked = checked
    }

    override fun onClick() {
        super.onClick()
        checked = !checked
    }

}