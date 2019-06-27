package com.sibedge.yokodzun.android.ui.list.base

import android.content.Context
import android.view.View
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.ui.cell.ItemCell
import com.sibedge.yokodzun.android.ui.cell.line.LineCell


open class ItemCellListContaner<T : Any>(
    context: Context,
    producer: Producer<GetterAsync<Unit, List<T>>>,
    onClick: (T) -> Unit,
    cellDataGetter: (T) -> LineCell.Data<T>,
    idGetter: (T) -> Any,
    onEmptyListInfoViewGenerator: () -> View,
    invalidator: () -> Unit
) : ItemsListContaner<T>(
    context = context,
    producer = producer,
    viewWrappersCreator = { ItemCell(context, onClick, cellDataGetter) },
    idGetter = idGetter,
    onEmptyListInfoViewGenerator = onEmptyListInfoViewGenerator,
    invalidator = invalidator
)