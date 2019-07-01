package com.sibedge.parameter.android.ui.view.cell

import android.content.Context
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.view.description.DescriptionView
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.Parameter
import ru.hnau.androidutils.ui.view.clickable.ClickableFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class ParameterView(
    context: Context,
    private val onClick: (Parameter) -> Unit
) : ClickableFrameLayout(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
), ViewWithContent<Parameter> {

    override val view = this

    private val descriptionView = DescriptionView(context)
        .applyPadding(SizeManager.DEFAULT_SEPARATION)

    override var data: Parameter? = null
        set(value) {
            field = value
            descriptionView.data = data?.description?.let { DescriptionView.Item(it) }
        }

    init {
        addView(descriptionView)
    }

    override fun onClick() {
        super.onClick()
        data?.let(onClick)
    }


}