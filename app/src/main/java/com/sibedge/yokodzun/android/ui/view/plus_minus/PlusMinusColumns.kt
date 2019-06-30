package com.sibedge.yokodzun.android.ui.view.plus_minus

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.dp_px.dp0
import ru.hnau.androidutils.context_getters.dp_px.dp32
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class PlusMinusColumns<T : Comparable<T>>(
    context: Context,
    initialValue: T,
    availableValueRange: ClosedRange<T>,
    columns: Collection<PlusMinusColumnInfo<T>>
) : LinearLayout(
    context
) {

    companion object {

        private val COLUMN_WIDTH = dp32
        private val COLUMNS_SEPARATION = SizeManager.SMALL_SEPARATION

    }

    private val valueProducerInner =
        ActualProducerSimple(initialValue)

    val valueProducer: Producer<T>
        get() = valueProducerInner

    init {
        applyHorizontalOrientation()
        applyCenterGravity()

        columns.forEachIndexed { index, (title, actionPlus, actionMinus) ->
            val endMargin = if (index < columns.size - 1) COLUMNS_SEPARATION else dp0
            addChild(
                PlusMinusColumn<T>(
                    context = context,
                    valueProducer = valueProducerInner,
                    onValueChanged = valueProducerInner::updateState,
                    availableValueRange = availableValueRange,
                    title = title,
                    actionMinus = actionMinus,
                    actionPlus = actionPlus
                )
            ) {
                applyLinearParams {
                    setWidth(COLUMN_WIDTH)
                    setEndMargin(endMargin)
                }
            }
        }
    }

}