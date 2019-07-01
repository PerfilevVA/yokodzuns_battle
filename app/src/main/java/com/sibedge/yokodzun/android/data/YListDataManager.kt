package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.common.data.helpers.ListItem
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.handle


abstract class YListDataManager<I, K : Comparable<K>, T : ListItem<I, K>>(
    valueLifetime: TimeValue? = DataUtils.DEFAULT_VALUE_LIFETIME
) : YDataManager<List<T>>(
    valueLifetime = valueLifetime
) {

    private fun Iterable<T>.sort() = sortedBy { it.sortKey }

    override suspend fun getValue() =
        getList().sort()

    protected abstract suspend fun getList(): List<T>

    protected fun insertItem(
        item: T
    ) = updateOrInvalidate { items ->
        (items + item).sort()
    }

    protected fun removeItem(
        id: I
    ) = updateOrInvalidate { items ->
        items.filter { item -> item.id != id }
    }

    protected fun updateOrInvalidateItem(
        id: K,
        update: T.() -> T
    ) = updateOrInvalidate {
        it.map {
            (it.id == id).handle(
                onTrue = { it.update() },
                onFalse = { it }
            )
        }.sort()
    }


}