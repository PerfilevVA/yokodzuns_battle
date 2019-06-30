package com.sibedge.yokodzun.android.ui.view.plus_minus

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyVerticalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class PlusMinusView<T : Comparable<T>>(
    context: Context,
    initialValue: T,
    availableValueRange: ClosedRange<T>,
    columns: Collection<PlusMinusColumnInfo<T>>,
    private val valueToStringConverter: (T) -> StringGetter
) : LinearLayout(context) {

    private val valueView = Label(
        context = context,
        gravity = HGravity.CENTER,
        fontType = FontManager.UBUNTU_BOLD,
        textColor = ColorManager.PRIMARY,
        textSize = SizeManager.TEXT_20,
        minLines = 1,
        maxLines = 1
    )
        .applyLinearParams {
            setBottomMargin(SizeManager.SMALL_SEPARATION)
        }

    private val columnsView =
        PlusMinusColumns(
            context,
            initialValue,
            availableValueRange,
            columns
        )

    val valueProducer: Producer<T>
        get() = columnsView.valueProducer

    init {
        applyVerticalOrientation()
        applyCenterGravity()
        addChild(valueView)
        addChild(columnsView)
        valueProducer.attach { valueView.text = valueToStringConverter(it) }
    }


}