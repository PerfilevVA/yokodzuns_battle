package com.sibedge.yokodzun.android.ui.view.cell

import android.content.Context
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.ui.view.description.DescriptionView
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.Yokodzun
import ru.hnau.androidutils.ui.view.clickable.ClickableLinearLayout
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLayoutParams
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class YokodzunView(
    context: Context,
    additionalButtonInfo: (Yokodzun) -> AdditionalButton.Info? = { null },
    private val onClick: (Yokodzun) -> Unit
) : ClickableLinearLayout(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
), ViewWithData<Yokodzun> {

    override val view = this

    private val descriptionView = DescriptionView(context)
        .applyLinearParams { setStretchedWidth() }
        .applyPadding(SizeManager.DEFAULT_SEPARATION)

    private val additionalButton = AdditionalButton<Yokodzun>(
        context = context,
        additionalButtonInfo = additionalButtonInfo
    ).applyLayoutParams { setMatchParentHeight() }

    override var data: Yokodzun? = null
        set(value) {
            field = value
            descriptionView.data = data?.description?.let { DescriptionView.Item(it) }
            additionalButton.data = data
        }

    init {
        applyHorizontalOrientation()
        addView(descriptionView)
        addView(additionalButton)
    }

    override fun onClick() {
        super.onClick()
        data?.let(onClick)
    }

}