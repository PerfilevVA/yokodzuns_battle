package com.sibedge.yokodzun.android.utils.extensions

import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import ru.hnau.androidutils.context_getters.dp_px.dp112
import ru.hnau.androidutils.ui.view.list.base.BaseListPaddingDecoration
import ru.hnau.jutils.helpers.Holder
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.takeIfPositive


fun RecyclerView.setBottomPaddingForPrimaryActionButtonDecoration(itemsProducer: Producer<*>) {
    addItemDecoration(BaseListPaddingDecoration(afterLast = dp112))
    itemsProducer.attach { notifyAdapterLastItemChanged() }
}

private val handler = Handler()

private fun RecyclerView.notifyAdapterLastItemChanged() {
    val adapter = this.adapter ?: return
    val size = adapter.itemCount.takeIfPositive() ?: return
    handler.post { adapter.notifyItemChanged(size - 1) }
}