package com.sibedge.yokodzun.android.layers.rate.list.item.view.rate

import android.content.Context
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItem
import com.sibedge.yokodzun.android.utils.RateUtils
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.dp_px.dp48
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.apply.applyBottomPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.jutils.coroutines.TasksFinalizer
import ru.hnau.jutils.coroutines.executor.InterruptableExecutor
import ru.hnau.jutils.ifNotNull
import ru.hnau.jutils.producer.ActualProducerSimple


class RateView(
    context: Context,
    executor: InterruptableExecutor
) : LinearLayout(
    context
), BaseListViewWrapper<RatesListItem> {

    companion object {

        private val PREFERRED_HEIGHT = dp48

    }

    override val view = this

    private val markProducer =
        ActualProducerSimple(0)

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val tasksFinalizer = TasksFinalizer(executor)

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
                    onMarkClick = this@RateView::onMarkClick
                ).applyLinearParams {
                    setSize(PREFERRED_HEIGHT)
                }
            )
        }
    }

    private fun onMarkClick(mark: Int) {
        markProducer.updateState(mark)
    }

    override fun setContent(content: RatesListItem, position: Int) {
        content.ifNotNull {
            val mark = content.rate!!.value.let(RateUtils::valueToMark).toInt()
            markProducer.updateState(mark)
        }
    }
}