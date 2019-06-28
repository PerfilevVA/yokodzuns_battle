package com.sibedge.yokodzun.android.ui.list.base

import android.content.Context
import ru.hnau.androidutils.ui.view.list.base.*
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.ui.list.YListItemsDevider
import com.sibedge.yokodzun.android.utils.extensions.setBottomPaddingForPrimaryActionButtonDecoration


class ItemsListView<T : Any>(
    context: Context,
    itemsProducer: Producer<List<T>>,
    viewWrappersCreator: (itemType: Int) -> BaseListViewWrapper<T>,
    idGetter: (T) -> Any
) : BaseList<T>(
    context = context,
    itemsProducer = itemsProducer,
    viewWrappersCreator = viewWrappersCreator,
    orientation = BaseListOrientation.VERTICAL,
    fixedSize = false,
    calculateDiffInfo = BaseListCalculateDiffInfo<T>(
        itemsComparator = { item1, item2 -> idGetter.invoke(item1) == idGetter.invoke(item2) },
        itemsContentComparator = { item1, item2 -> item1 == item2 }
    ),
    itemsDecoration = YListItemsDevider.create(context)
) {

    init {
        setBottomPaddingForPrimaryActionButtonDecoration()
    }

}