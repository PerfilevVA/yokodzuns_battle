package com.sibedge.yokodzun.android.layers.rate.list.item.view.rate

import android.content.Context
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.data.RaterRatesDataManager
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItem
import com.sibedge.yokodzun.android.utils.RateUtils
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.dp_px.dp48
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.apply.applyBottomPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.ifNotNull
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.StateProducerSimple


class RateView(
    context: Context
) : LinearLayout(
    context
), BaseListViewWrapper<RatesListItem> {

    companion object {

        private val PREFERRED_HEIGHT = dp48

    }

    override val view = this

    private val keyProducer = StateProducerSimple<RaterRatesDataManager.Key>()

    private val markProducer = ActualProducerSimple(0)

    private val syncedMarkProducer = ActualProducerSimple(0)

    init {
        applyHorizontalOrientation()
        applyHorizontalPadding(SizeManager.DEFAULT_SEPARATION)
        applyBottomPadding(SizeManager.SMALL_SEPARATION)
        repeat(RateUtils.MARKS_COUNT) { i ->
            val itemMark = i + 1
            addView(
                RateItemSwitcher(
                    context = context,
                    mark = itemMark,
                    itemIsSelectedProducer = markProducer.map { mark ->
                        itemMark <= mark
                    },
                    itemIsTouchableProducer = syncedMarkProducer.map { mark ->
                        mark == 0
                    },
                    onMarkClick = this@RateView::onMarkClick
                ).applyLinearParams {
                    setSize(PREFERRED_HEIGHT)
                }
            )
        }
    }

    private fun onMarkClick(mark: Int) {
        RaterRatesDataManager.UnsyncRatesContainer.
            put(keyProducer.currentState!!, RateUtils.markToValue(mark.toFloat()))
        markProducer.updateState(mark)
    }

    override fun setContent(content: RatesListItem, position: Int) {
        val ratePair = content.rate!!
        val key = ratePair.first
        val value = ratePair.second

        keyProducer.updateState(key)
        value.ifNotNull {
            val mark = RateUtils.valueToMark(it).toInt()
            markProducer.updateState(mark)
            syncedMarkProducer.updateState(mark)
        }
    }
}