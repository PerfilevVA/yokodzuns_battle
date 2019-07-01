package com.sibedge.yokodzun.android.ui.view

import android.content.Context
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.utils.ColorTriple
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


class CountView(
    context: Context,
    title: StringGetter
) : LinearLayout(
    context
), ViewWithData<CountView.Info> {

    data class Info(
        val count: Int,
        val color: ColorTriple = ColorManager.PRIMARY_TRIPLE
    )

    override val view = this

    override var data: Info? = null
        set(value) {
            if (field != value) {
                field = value
                updateData(value)
            }
        }

    private val textView = Label(
        context = context,
        info = LabelInfo(
            textSize = SizeManager.TEXT_12,
            fontType = FontManager.DEFAULT,
            maxLines = 1,
            minLines = 1,
            gravity = HGravity.START_CENTER_VERTICAL
        ),
        initialText = title
    ).applyLinearParams {
        setEndMargin(SizeManager.SMALL_SEPARATION)
    }

    private val chipLabel = ChipLabel(
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
        addView(chipLabel)
    }

    private fun updateData(info: Info?) {

        val count = info?.count?.takeIfPositive()
        if (count == null) {
            setGone()
            return
        }

        setVisible()
        val color = info.color
        textView.textColor = color.main
        chipLabel.data = ChipLabel.Info(
            text = count.toString().toGetter(),
            color = color
        )

    }

}