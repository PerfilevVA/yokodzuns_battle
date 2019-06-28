package com.sibedge.yokodzun.android.ui.sections.item

import android.content.Context
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.description.DescriptionView
import com.sibedge.yokodzun.android.ui.sections.SectionsTreeUtils
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter.Companion.dp
import ru.hnau.androidutils.context_getters.dp_px.dp32
import ru.hnau.androidutils.context_getters.dp_px.dp48
import ru.hnau.androidutils.context_getters.dp_px.dp64
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class SectionsTreeItemContentView(
    context: Context,
    additionalButton: (Section) -> AdditionalButton.Info?
) : LinearLayout(
    context
), ViewWithContent<TreeSection> {

    override val view = this

    private val descriptionView = DescriptionView(context)
        .applyPadding(SizeManager.DEFAULT_SEPARATION)
        .applyLinearParams { setStretchedWidth() }

    private val additionalButtonView = AdditionalButton(
        context = context,
        additionalButtonInfo = additionalButton
    ).applyLinearParams {
        setWidth(dp(44))
        setMatchParentHeight()
    }

    override var content: TreeSection? = null
        set(value) {
            field = value
            descriptionView.content = value?.let {
                DescriptionView.Item(
                    description = it.section.description,
                    mainColor = SectionsTreeUtils.getOffsetColor(it.depth)
                )
            }
            additionalButtonView.setSection(content?.section)
        }

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        addChild(descriptionView)
        addChild(additionalButtonView)
    }

}