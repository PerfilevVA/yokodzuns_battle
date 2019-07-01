package com.sibedge.yokodzun.android.ui.view.list.base.async

import android.content.Context
import android.view.View
import com.sibedge.yokodzun.android.ui.ViewWithData
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer


class AsyncViewsWithContentListContainer<T : Any>(
    context: Context,
    producer: Producer<GetterAsync<Unit, List<T>>>,
    viewWithDataGenerator: () -> ViewWithData<T>,
    idGetter: (T) -> Any,
    onEmptyListInfoViewGenerator: () -> View,
    invalidator: () -> Unit
) : AsyncItemsListContaner<T>(
    context = context,
    producer = producer,
    viewWrappersCreator = {
        val viewWithContent = viewWithDataGenerator()
        object : BaseListViewWrapper<T> {
            override val view = viewWithContent.view
            override fun setContent(content: T, position: Int) {
                viewWithContent.data = content
            }
        }
    },
    idGetter = idGetter,
    onEmptyListInfoViewGenerator = onEmptyListInfoViewGenerator,
    invalidator = invalidator
)