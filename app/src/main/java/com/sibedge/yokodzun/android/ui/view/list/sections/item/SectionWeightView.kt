package com.sibedge.yokodzun.android.ui.view.list.sections.item

import android.content.Context
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.view.ChipLabel
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.view.list.sections.SectionsTreeUtils
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.utils.apply.applyEndGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.setGone
import ru.hnau.androidutils.ui.view.utils.setVisible
import ru.hnau.jutils.takeIfPositive


class SectionWeightView(
    context: Context
) : LinearLayout(
    context
), ViewWithContent<TreeSection> {

    override val view = this

    override var data: TreeSection? = null
        set(value) {
            if (field != value) {
                field = value
                updateSection(value)
            }
        }

    private val textView = Label(
        context = context,
        info = LabelInfo(
            textSize = SizeManager.TEXT_12,
            fontType = FontManager.DEFAULT,
            maxLines = 1,
            minLines = 1,
            gravity = HGravity.END_CENTER_VERTICAL
        ),
        initialText = StringGetter(R.string.entity_section_property_weight)
    ).applyLinearParams {
        setStretchedWidth()
        setEndMargin(SizeManager.SMALL_SEPARATION)
    }

    private val weightView = ChipLabel(
        context = context,
        info = LabelInfo(
            textSize = SizeManager.TEXT_12,
            textColor = ColorManager.FG,
            fontType = FontManager.BOLD,
            gravity = HGravity.CENTER,
            maxLines = 1,
            minLines = 1
        )
    )

    init {
        applyHorizontalOrientation()
        applyEndGravity()
        addView(textView)
        addView(weightView)
    }

    private fun updateSection(section: TreeSection?) {

        val weight = section?.section?.weight?.takeIfPositive()
        if (weight == null) {
            setGone()
            return
        }

        setVisible()
        val color = SectionsTreeUtils.getOffsetColor(section.depth)
        textView.textColor = color.main
        weightView.data = ChipLabel.Info(
            text = weight.toString().toGetter(),
            color = color
        )

    }

}