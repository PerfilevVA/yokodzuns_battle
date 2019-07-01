package com.sibedge.yokodzun.android.ui.view.list.sections.item

import android.content.Context
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.ui.view.description.DescriptionView
import com.sibedge.yokodzun.android.ui.view.list.sections.SectionsTreeUtils
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter.Companion.dp
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLayoutParams
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class SectionContentView(
    context: Context,
    additionalButton: (Section) -> AdditionalButton.Info?
) : LinearLayout(
    context
), ViewWithContent<TreeSection> {

    override val view = this

    private val descriptionView = DescriptionView(context)

    private val weightView = SectionWeightView(
        context
    )
        .applyLinearParams {
            setMatchParentWidth()
            setTopMargin(SizeManager.SMALL_SEPARATION)
        }

    private val additionalButtonView = AdditionalButton(
        context = context,
        additionalButtonInfo = additionalButton
    ).applyLayoutParams { setMatchParentHeight() }

    override var data: TreeSection? = null
        set(value) {
            field = value
            descriptionView.data = value?.let {
                DescriptionView.Item(
                    description = it.section.description,
                    mainColor = SectionsTreeUtils.getOffsetColor(it.depth).main
                )
            }
            weightView.data = value
            additionalButtonView.data = value?.section
        }

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        addVerticalLayout {
            applyPadding(SizeManager.DEFAULT_SEPARATION)
            applyLinearParams { setStretchedWidth() }
            addChild(descriptionView)
            addChild(weightView)
        }
        addChild(additionalButtonView)
    }

}