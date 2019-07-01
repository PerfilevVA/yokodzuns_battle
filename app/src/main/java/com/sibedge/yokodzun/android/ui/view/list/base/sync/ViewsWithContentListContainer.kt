package com.sibedge.yokodzun.android.ui.view.list.base.sync

import android.content.Context
import android.view.View
import com.sibedge.yokodzun.android.ui.ViewWithData
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.jutils.producer.Producer


class ViewsWithContentListContainer<T : Any>(
    context: Context,
    producer: Producer<List<T>>,
    viewWithDataGenerator: () -> ViewWithData<T>,
    idGetter: (T) -> Any,
    onEmptyListInfoView: View
) : ItemsListContaner<T>(
    context = context,
    viewWrappersCreator = {
        val viewWithContent = viewWithDataGenerator()
        object : BaseListViewWrapper<T> {
            override val view = viewWithContent.view
            override fun setContent(content: T, position: Int) {
                viewWithContent.data = content
            }
        }
    },
    producer = producer,
    idGetter = idGetter,
    onEmptyListInfoView = onEmptyListInfoView
)