package com.sibedge.yokodzun.android.ui.view.list.sections.item

import android.content.Context
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.ui.view.CountView
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.ui.view.description.DescriptionView
import com.sibedge.yokodzun.android.ui.view.list.sections.SectionsTreeUtils
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLayoutParams
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class SectionContentView(
    context: Context,
    additionalButtonInfoCreator: ((Section) -> AdditionalButton.Info?)?,
    additionalButtonColor: ColorTriple = ColorManager.PRIMARY_TRIPLE
) : LinearLayout(
    context
), ViewWithData<TreeSection> {

    override val view = this

    private val descriptionView = DescriptionView(context)

    private val weightView = CountView(
        context = context,
        title = StringGetter(R.string.entity_section_property_weight)
    )
        .applyLinearParams {
            setEndGravity()
            setTopMargin(SizeManager.SMALL_SEPARATION)
        }

    private val additionalButtonView =
        additionalButtonInfoCreator?.let {
            AdditionalButton(
                context = context,
                additionalButtonInfo = it
            ).applyLayoutParams { setMatchParentHeight() }
        }

    override var data: TreeSection? = null
        set(value) {
            field = value
            descriptionView.data = value?.let {
                DescriptionView.Item(
                    description = it.section.description,
                    mainColor = SectionsTreeUtils.getOffsetColor(it.depth).main
                )
            }
            weightView.data = value?.let {
                CountView.Info(
                    count = it.section.weight,
                    color = SectionsTreeUtils.getOffsetColor(it.depth)
                )
            }
            additionalButtonView?.data = value?.section
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