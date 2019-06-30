package com.sibedge.yokodzun.android.ui.plus_minus

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyVerticalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class PlusMinusColumn<T : Comparable<T>>(
    context: Context,
    title: StringGetter,
    private val valueProducer: Producer<T>,
    private val availableValueRange: ClosedRange<T>,
    private val actionPlus: (T) -> T,
    private val actionMinus: (T) -> T,
    private val onValueChanged: (T) -> Unit
) : LinearLayout(
    context
) {

    private val plusButton =
        createPlusOrMinusButton(
            icon = DrawableGetter(R.drawable.ic_up_primary),
            action = actionPlus
        )

    private val titleView = Label(
        context = context,
        initialText = title,
        maxLines = 1,
        minLines = 1,
        textSize = SizeManager.TEXT_16,
        textColor = ColorManager.FG,
        fontType = FontManager.DEFAULT,
        gravity = HGravity.CENTER
    )
        .applyLinearParams {
            setStretchedHeight()
        }

    private val minusButton =
        createPlusOrMinusButton(
            icon = DrawableGetter(R.drawable.ic_down_primary),
            action = actionMinus
        )

    init {
        applyVerticalOrientation()
        applyCenterGravity()
        addChild(plusButton)
        addChild(titleView)
        addChild(minusButton)
    }

    private fun createPlusOrMinusButton(
        icon: DrawableGetter,
        action: (T) -> T
    ) = PlusMinusButton<T>(
        context = context,
        icon = icon,
        action = action,
        availableValueRange = availableValueRange,
        onValueChanged = onValueChanged,
        valueProducer = valueProducer
    )

}