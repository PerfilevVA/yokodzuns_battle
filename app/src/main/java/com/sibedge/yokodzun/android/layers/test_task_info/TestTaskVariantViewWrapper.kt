package com.sibedge.yokodzun.android.layers.test_task_info

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.setGone
import ru.hnau.androidutils.ui.view.utils.setVisible
import ru.hnau.jutils.handle
import ru.hnau.jutils.takeIfNotEmpty
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.entity.TestTaskVariantWithId
import com.sibedge.yokodzun.android.ui.cell.Cell
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.label.CellLabel
import com.sibedge.yokodzun.android.ui.cell.label.CellSubtitle
import com.sibedge.yokodzun.android.ui.cell.label.CellTitle
import com.sibedge.yokodzun.android.utils.extensions.*
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.remote_teaching_common.data.test.TestTaskType


class TestTaskVariantViewWrapper(
    context: Context,
    private val taskType: TestTaskType,
    onClick: (TestTaskVariantWithId) -> Unit,
    private val onMenuClick: (TestTaskVariantWithId) -> Unit
) : Cell<TestTaskVariantWithId>(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO,
    onClick = onClick
) {

    companion object {

        private val ADDITIONAL_ACTION_BUTTON_ICON = DrawableGetter(R.drawable.ic_options_primary)

    }

    private val titleView = CellTitle(
        context = context,
        activeColor = ColorManager.PRIMARY,
        inactiveColor = ColorManager.PRIMARY
    )

    private val descriptionMDView = CellSubtitle(
        context = context,
        activeColor = ColorManager.PRIMARY,
        inactiveColor = ColorManager.PRIMARY
    )
        .applyTopPadding(SizeManager.SMALL_SEPARATION)

    private val optionsContainer = LinearLayout(context)
        .applyVerticalOrientation()
        .applyTopPadding(SizeManager.SMALL_SEPARATION)

    private val responseView = CellSubtitle(
        context = context,
        activeColor = ColorManager.PRIMARY,
        inactiveColor = ColorManager.PRIMARY
    )
        .applyTopPadding(SizeManager.SMALL_SEPARATION)

    private val mainContentContainer = LinearLayout(context)
        .applyVerticalOrientation()
        .applyLinearParams { setStretchedWidth() }
        .applyStartTopGravity()
        .applyPadding(SizeManager.DEFAULT_SEPARATION, SizeManager.SMALL_SEPARATION)
        .addChild(titleView)
        .addChild(descriptionMDView)
        .addChild(optionsContainer)
        .addChild(responseView)

    private val additionalActionButton =
        CellAdditionalActionButton(
            context = context,
            rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
        )
            .applyLinearParams { setMatchParentHeight() }

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        addChild(mainContentContainer)
        addChild(additionalActionButton)
    }

    override fun onContentReceived(content: TestTaskVariantWithId) {

        titleView.info = CellLabel.Info(
            text = StringGetter(R.string.test_task_info_layer_item_title, content.numberUiString)
        )

        val variant = content.value

        descriptionMDView.info = CellLabel.Info(
            text = StringGetter(R.string.test_task_info_layer_item_description, variant.descriptionMD)
        )

        optionsContainer.removeAllViews()
        variant.optionsMD.takeIfNotEmpty().handle(
            ifNull = {
                optionsContainer.setGone()
            },
            ifNotNull = { options ->
                optionsContainer.setVisible()
                options.forEach { option ->
                    val optionView = CellSubtitle(
                        context = context,
                        activeColor = ColorManager.PRIMARY,
                        inactiveColor = ColorManager.PRIMARY
                    ).apply {
                        info = CellLabel.Info(option.toGetter())
                    }
                    optionsContainer.addChild(optionView)
                }
            }

        )

        val responseString = variant.responseToUiString(taskType)
        responseView.info = CellLabel.Info(
            text = StringGetter(R.string.test_task_info_layer_item_response, responseString)
        )

        additionalActionButton.info = CellAdditionalActionButton.Info(
            icon = ADDITIONAL_ACTION_BUTTON_ICON,
            onClick = { onMenuClick.invoke(content) }
        )
    }

}
