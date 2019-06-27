package com.sibedge.yokodzun.android.layers.test_task_variant_info

import android.content.Context
import android.view.View
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.getter.toGetter
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.entity.TestTaskVariantOptionWithId
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.line.LineCell
import com.sibedge.yokodzun.android.ui.list.base.ItemCellListContaner
import com.sibedge.yokodzun.android.ui.list.base.ItemsListContaner
import ru.hnau.remote_teaching_common.data.test.TestTaskType


class TestTaskVariantOptionsContainer(
    context: Context,
    producer: Producer<GetterAsync<Unit, List<TestTaskVariantOptionWithId>>>,
    onClick: (TestTaskVariantOptionWithId) -> Unit,
    onDeleteClick: (TestTaskVariantOptionWithId) -> Unit,
    onEmptyListInfoViewGenerator: () -> View,
    invalidator: () -> Unit
) : ItemCellListContaner<TestTaskVariantOptionWithId>(
    context = context,
    idGetter = TestTaskVariantOptionWithId::number,
    producer = producer,
    onClick = onClick,
    onEmptyListInfoViewGenerator = onEmptyListInfoViewGenerator,
    invalidator = invalidator,
    cellDataGetter = { option ->
        LineCell.Data(
            title = option.value.toGetter(),
            additionalActionButtonInfo = CellAdditionalActionButton.Info(
                icon = DELETE_ICON,
                onClick = { onDeleteClick(option) }
            )
        )
    }
) {

    companion object {

        private val DELETE_ICON = DrawableGetter(R.drawable.ic_delete_primary)

    }

}