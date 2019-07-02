package com.sibedge.yokodzun.android.layers.rate.list.item.view.rate

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import ru.hnau.androidutils.ui.utils.Side
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.view_presenter.*
import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.observeWhen


class RateItemSwitcher(
    context: Context,
    mark: Int,
    itemIsSelectedProducer: Producer<Boolean>,
    onMarkClick: (Int) -> Unit
) : FrameLayout(
    context
) {

    private val notSelectedView by lazy {
        RateItemView(
            context,
            mark,
            false,
            onMarkClick
        )
    }
    private val selectedView by lazy {
        RateItemView(
            context,
            mark,
            true,
            onMarkClick
        )
    }

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    init {
        itemIsSelectedProducer.observeWhen(isVisibleToUserProducer) { selected ->
            removeAllViews()
            addView(
                selected.handle(
                    onTrue = { selectedView },
                    onFalse = { notSelectedView }
                )
            )
        }
    }

}