package com.sibedge.yokodzun.android.layers.rates

import android.content.Context
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.utils.RateCalculator
import com.sibedge.yokodzun.android.utils.RateUtils
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.dp_px.dp48
import ru.hnau.androidutils.context_getters.dp_px.dp64
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.apply.addHorizontalLayout
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyVerticalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.setInvisible
import ru.hnau.androidutils.ui.view.utils.setVisible
import ru.hnau.jutils.handle


class TeamRateView(
    context: Context
) : LinearLayout(
    context
), BaseListViewWrapper<RateCalculator.Value> {

    override val view = this

    private val teamNameView = Label(
        context = context,
        maxLines = 1,
        minLines = 1,
        fontType = FontManager.DEFAULT,
        gravity = HGravity.START_CENTER_VERTICAL,
        textSize = SizeManager.TEXT_16,
        textColor = ColorManager.PRIMARY
    )

    private val markView = Label(
        context = context,
        maxLines = 1,
        minLines = 1,
        fontType = FontManager.BOLD,
        gravity = HGravity.CENTER,
        textSize = SizeManager.TEXT_20
    ).applyLinearParams {
        setWidth(dp64)
    }

    private val valueView = TeamRateValueView(context)
        .applyLinearParams {
            setStretchedWidth()
            setHorizontalMargins(SizeManager.DEFAULT_SEPARATION)
        }

    private val marksCountView = Label(
        context = context,
        maxLines = 1,
        minLines = 1,
        fontType = FontManager.REGULAR,
        gravity = HGravity.CENTER,
        textSize = SizeManager.TEXT_16,
        textColor = ColorManager.FG
    ).applyLinearParams {
        setWidth(dp48)
    }

    init {
        applyVerticalOrientation()
        applyPadding(SizeManager.DEFAULT_SEPARATION)

        addView(teamNameView)

        addHorizontalLayout {

            applyLinearParams {
                setMatchParentWidth()
                setTopMargin(SizeManager.DEFAULT_SEPARATION)
            }

            addView(markView)
            addView(valueView)
            addView(marksCountView)

        }
    }

    override fun setContent(content: RateCalculator.Value, position: Int) {
        teamNameView.text = content.team.description.title.toGetter()
        content.value.handle(
            ifNotNull = { value ->
                markView.setVisible()
                val mark = RateUtils.valueToMark(value)
                val normalizedMark = (mark * 100).toInt() / 100f
                markView.text = normalizedMark.toString().toGetter()
                markView.textColor = RateUtils.getValueColor(value)
            },
            ifNull = {
                markView.setInvisible()
            }
        )
        valueView.data = content.value
        marksCountView.text = content.marksCount.toString().toGetter()
    }

}