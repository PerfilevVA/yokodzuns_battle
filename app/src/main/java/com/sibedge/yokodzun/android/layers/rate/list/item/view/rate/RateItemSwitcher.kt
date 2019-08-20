package com.sibedge.yokodzun.android.layers.rate.list.item.view.rate

import android.content.Context
import android.widget.FrameLayout
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine
import ru.hnau.jutils.producer.extensions.observeWhen


class RateItemSwitcher(
    context: Context,
    mark: Int,
    itemIsSelectedProducer: Producer<Boolean>,
    itemIsTouchableProducer: Producer<Boolean>,
    onMarkClick: (Int) -> Unit
) : FrameLayout(
    context
) {
    private val selectedView by lazy {
        RateItemView(
            context,
            mark,
            onMarkClick,
            selected = true,
            touchable = true
        )
    }

    private val notSelectedView by lazy {
        RateItemView(
            context,
            mark,
            onMarkClick,
            selected = false,
            touchable = true
        )
    }

    private val selectedUntouchableView by lazy {
        RateItemView(
            context,
            mark,
            onMarkClick = { },
            selected = true,
            touchable = false
        )
    }

    private val notSelectedUntouchableView by lazy {
        RateItemView(
            context,
            mark,
            onMarkClick = { },
            selected = false,
            touchable = false
        )
    }

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    init {
        Producer.combine(
            producer1 = itemIsSelectedProducer,
            producer2 = itemIsTouchableProducer
        ) { selected, touchable ->
            Pair(
                selected,
                touchable
            )
        }.observeWhen(isVisibleToUserProducer) { viewTypePair ->
            val selected = viewTypePair.first
            val touchable = viewTypePair.second
            removeAllViews()
            addView(
                selected.handle(
                    onTrue = {
                        touchable.handle(
                            onTrue = { selectedView },
                            onFalse = { selectedUntouchableView }
                        )
                    },
                    onFalse = {
                        touchable.handle(
                            onTrue = { notSelectedView },
                            onFalse = { notSelectedUntouchableView }
                        )
                    }
                )
            )
        }
    }
}