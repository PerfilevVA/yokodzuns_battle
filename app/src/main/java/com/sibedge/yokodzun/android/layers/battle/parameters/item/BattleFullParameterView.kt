package com.sibedge.yokodzun.android.layers.battle.parameters.item

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.view.CountView
import com.sibedge.yokodzun.android.ui.view.description.DescriptionView
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.clickable.ClickableLinearLayout
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyVerticalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class BattleFullParameterView(
    context: Context,
    private val onClick: (BattleFullParameter) -> Unit
) : ClickableLinearLayout(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO
), BaseListViewWrapper<BattleFullParameter> {

    override val view = this

    private val descriptionView = DescriptionView(context)

    private val weightView = CountView(
        context = context,
        title = StringGetter(R.string.entity_parameter_property_weight)
    ).applyLinearParams {
        setEndGravity()
        setTopMargin(SizeManager.SMALL_SEPARATION)
    }

    private var battleFullParameter: BattleFullParameter? = null

    init {
        applyVerticalOrientation()
        applyPadding(SizeManager.DEFAULT_SEPARATION)
        addView(descriptionView)
        addView(weightView)
    }

    override fun setContent(content: BattleFullParameter, position: Int) {
        battleFullParameter = content
        descriptionView.data = DescriptionView.Item(content.parameter.description)
        weightView.data = CountView.Info(content.weight)
    }

    override fun onClick() {
        super.onClick()
        battleFullParameter?.let(onClick)
    }

}