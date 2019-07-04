package com.sibedge.yokodzun.android.ui.view.list.sections.item

import android.content.Context
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.ui.view.clickable.ClickableLinearLayout
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class SectionView(
    context: Context,
    additionalButtonInfoCreator: ((Section) -> AdditionalButton.Info?)?,
    additionalButtonColor: ColorTriple = ColorManager.PRIMARY_TRIPLE,
    private val onClick: (TreeSection) -> Unit
) : ClickableLinearLayout(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
), BaseListViewWrapper<TreeSection> {

    override val view = this

    private val offsetView =
        SectionsTreeOffsetView(context)

    private val contentView = SectionContentView(
        context = context,
        additionalButtonInfoCreator = additionalButtonInfoCreator,
        additionalButtonColor = additionalButtonColor
    ).applyLinearParams { setStretchedWidth() }

    private var section: TreeSection? = null
        set(value) {
            field = value
            offsetView.data = value?.depth
            contentView.data = value
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