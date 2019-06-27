package com.sibedge.yokodzun.android.ui.plus_minus

import android.content.Context
import android.util.Range
import ru.hnau.androidutils.animations.smooth
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.observeWhen
import ru.hnau.jutils.producer.extensions.toFloat
import com.sibedge.yokodzun.android.ui.button.SimpleIconButton


class PlusMinusButton<T : Comparable<T>>(
    context: Context,
    valueProducer: Producer<T>,
    private val availableValueRange: ClosedRange<T>,
    icon: DrawableGetter,
    private val action: (T) -> T,
    private val onValueChanged: (T) -> Unit
) : SimpleIconButton(
    context = context,
    icon = icon
) {

    private var nextValue: T? = null

    init {

        val nextValueProducer = valueProducer
            .observeWhen(createIsVisibleToUserProducer())
            .map(action)

        nextValueProducer
            .map { it in availableValueRange }
            .toFloat()
            .smooth()
            .attach { alpha = it }

        nextValueProducer
            .attach { nextValue = it }
    }

    override fun onClick() {
        super.onClick()
        nextValue
            ?.takeIf { it in availableValueRange }
            ?.let(onValueChanged)
    }

}