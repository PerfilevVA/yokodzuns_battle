package com.sibedge.yokodzun.android.ui.view.cell.battle

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.ui.view.description.DescriptionView
import com.sibedge.yokodzun.android.utils.extensions.color
import com.sibedge.yokodzun.android.utils.extensions.title
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.clickable.ClickableLinearLayout
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class BattleView(
    context: Context,
    private val onClick: (Battle) -> Unit,
    onYoconzunsCountClicked: (Battle) -> Unit,
    onParametersCountClicked: (Battle) -> Unit,
    onSectionsCountClicked: (Battle) -> Unit
) : ClickableLinearLayout(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
), ViewWithData<Battle> {

    override val view = this

    override var data: Battle? = null
        set(value) {
            field = value
            updateContent(value)
        }

    private val descriptionView = DescriptionView(
        context
    )
        .applyLinearParams {
            setMatchParentWidth()
            setBottomMargin(SizeManager.DEFAULT_SEPARATION)
        }
        .applyHorizontalPadding(SizeManager.DEFAULT_SEPARATION)

    private val statusView = Label(
        context = context,
        minLines = 1,
        maxLines = 1,
        gravity = HGravity.START_CENTER_VERTICAL,
        fontType = FontManager.BOLD,
        textSize = SizeManager.TEXT_16
    )

    private val countViews = listOf(
        BattleViewUtils.createTeamsCountView(context, onYoconzunsCountClicked),
        BattleViewUtils.createParametersCountView(context, onParametersCountClicked),
        BattleViewUtils.createSectionsCountView(context, onSectionsCountClicked)
    )

    init {
        applyVerticalOrientation()
        applyVerticalPadding(SizeManager.DEFAULT_SEPARATION)

        addView(descriptionView)

        addHorizontalLayout {
            applyBottomPadding(SizeManager.SMALL_SEPARATION)
            applyHorizontalPadding(SizeManager.DEFAULT_SEPARATION)
            applyTopPadding(SizeManager.EXTRA_SMALL_SEPARATION)
            applyBottomPadding(SizeManager.SMALL_SEPARATION)
            addLabel(
                text = StringGetter(R.string.battle_view_title_status) + ": ",
                minLines = 1,
                maxLines = 1,
                gravity = HGravity.START_CENTER_VERTICAL,
                fontType = FontManager.DEFAULT,
                textSize = SizeManager.TEXT_16,
                textColor = ColorManager.FG
            )
            addView(statusView)
        }

        addHorizontalLayout {
            applyHorizontalPadding(SizeManager.SMALL_SEPARATION)
            countViews.forEachIndexed { i, countView ->
                addView(countView.view)
                if (i < countViews.size - 1) {
                    addLinearSeparator()
                }
            }
        }
    }

    private fun updateContent(content: Battle?) {
        descriptionView.data = content?.description?.let { DescriptionView.Item(it) }
        countViews.forEach { it.data = content }
        statusView.textColor = content?.status?.color ?: ColorManager.TRANSPARENT
        statusView.text = content?.status?.title ?: StringGetter.EMPTY
    }

    override fun onClick() {
        super.onClick()
        data?.let(onClick)
    }


}