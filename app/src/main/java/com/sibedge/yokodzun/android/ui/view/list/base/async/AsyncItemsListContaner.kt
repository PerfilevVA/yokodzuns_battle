package com.sibedge.yokodzun.android.ui.view.list.base.async

import android.content.Context
import android.view.View
import ru.hnau.androidutils.ui.view.auto_swipe_refresh_view.AbstractAutoSwipeRefreshView
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.scroll.recycle_view.createOnRecyclerViewScrolledProducer
import ru.hnau.androidutils.ui.view.utils.scroll.recycle_view.createRecycleViewIsScrolledToBottomProducer
import ru.hnau.androidutils.ui.view.utils.scroll.recycle_view.createRecycleViewIsScrolledToTopProducer
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.SimpleDataProducer
import com.sibedge.yokodzun.android.ui.view.addSuspendPresenter
import com.sibedge.yokodzun.android.ui.view.list.base.ItemsListView
import com.sibedge.yokodzun.android.utils.managers.ColorManager


open class AsyncItemsListContaner<T : Any>(
    context: Context,
    private val producer: Producer<GetterAsync<Unit, List<T>>>,
    viewWrappersCreator: (itemType: Int) -> BaseListViewWrapper<T>,
    idGetter: (T) -> Any,
    onEmptyListInfoViewGenerator: () -> View,
    private val invalidator: () -> Unit
) : AbstractAutoSwipeRefreshView(
    context = context,
    color = ColorManager.PRIMARY
) {

    private val onEmptyListInfoView: View
            by lazy(onEmptyListInfoViewGenerator)

    private val itemsProducer =
        SimpleDataProducer<List<T>>(emptyList())

    private val listView =
        ItemsListView(
            context = context,
            itemsProducer = itemsProducer,
            viewWrappersCreator = viewWrappersCreator,
            idGetter = idGetter
        )

    private val onListScrolledProducer =
        listView.createOnRecyclerViewScrolledProducer()

    val onListScrolledToTopProducer =
        listView.createRecycleViewIsScrolledToTopProducer(onListScrolledProducer)

    val onListScrolledToBottomProducer =
        listView.createRecycleViewIsScrolledToBottomProducer(onListScrolledProducer)

    init {

        addSuspendPresenter(
            producer = producer,
            contentViewGenerator = this::createView,
            invalidator = invalidator
        )

    }

    private fun createView(values: List<T>): View {
        if (values.isEmpty()) {
            return onEmptyListInfoView
        }
        itemsProducer.content = values
        return listView
    }

    override fun updateContent() =
        invalidator.invoke()

}