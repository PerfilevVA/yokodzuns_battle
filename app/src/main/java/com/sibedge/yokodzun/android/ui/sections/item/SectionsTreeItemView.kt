package com.sibedge.yokodzun.android.ui.sections.item

import android.content.Context
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.ui.view.clickable.ClickableLinearLayout
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class SectionsTreeItemView(
    context: Context,
    additionalButton: (Section) -> AdditionalButton.Info?,
    private val onClick: (TreeSection) -> Unit
) : ClickableLinearLayout(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
), BaseListViewWrapper<TreeSection> {

    override val view = this

    private val offsetView = SectionsTreeItemOffsetView(context)
    private val contentView = SectionsTreeItemContentView(context, additionalButton)
        .applyLinearParams { setStretchedWidth() }

    private var section: TreeSection? = null
        set(value) {
            field = value
            offsetView.content = value?.depth
            contentView.content = value
        }

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        addChild(offsetView)
        addChild(contentView)
    }

    override fun setContent(content: TreeSection, position: Int) {
        section = content
    }

    override fun onClick() {
        super.onClick()
        section?.let(onClick)
    }

}