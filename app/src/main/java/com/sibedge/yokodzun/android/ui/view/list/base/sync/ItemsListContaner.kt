package com.sibedge.yokodzun.android.ui.view.list.base.sync

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
import ru.hnau.androidutils.ui.view.view_presenter.*
import ru.hnau.jutils.handle


open class ItemsListContaner<T : Any> private constructor(
    context: Context,
    producer: Producer<List<T>>,
    onEmptyListInfoView: View,
    listView: RecyclerView
) : PresenterView(
    context = context,
    presentingViewProducer = producer.map { itemsList ->
        itemsList.isEmpty().handle(
            onTrue = { onEmptyListInfoView },
            onFalse = { listView }
        ).toPresentingInfo(ColorManager.DEFAULT_PRESENTING_VIEW_PROPERTIES)
    },
    presenterViewInfo = PresenterViewInfo(
        horizontalSizeInterpolator = SizeInterpolator.MAX,
        verticalSizeInterpolator = SizeInterpolator.MAX
    )
) {

    constructor(
        context: Context,
        producer: Producer<List<T>>,
        viewWrappersCreator: (itemType: Int) -> BaseListViewWrapper<T>,
        idGetter: (T) -> Any,
        onEmptyListInfoView: View
    ): this(
        context = context,
        producer = producer,
        onEmptyListInfoView = onEmptyListInfoView,
        listView = ItemsListView(
            context = context,
            itemsProducer = producer,
            viewWrappersCreator = viewWrappersCreator,
            idGetter = idGetter
        )

    )

    private val onListScrolledProducer
            by lazy { listView.createOnRecyclerViewScrolledProducer() }

    val onListScrolledToTopProducer
            by lazy { listView.createRecycleViewIsScrolledToTopProducer(onListScrolledProducer) }

    val onListScrolledToBottomProducer
            by lazy { listView.createRecycleViewIsScrolledToBottomProducer(onListScrolledProducer) }


}