package com.sibedge.yokodzun.android.layers.students_group_test_results

import android.content.Context
import android.view.View
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.ui.list.base.ItemCellListContaner
import com.sibedge.yokodzun.android.ui.list.base.ItemsListContaner
import com.sibedge.yokodzun.android.utils.extensions.getLineCellData
import ru.hnau.remote_teaching_common.data.User


class StudentsGroupTestResultsListContainer(
    context: Context,
    producer: Producer<GetterAsync<Unit, List<StudentsGroupTestResultsListItem>>>,
    onClick: (StudentsGroupTestResultsListItem) -> Unit,
    onEmptyListInfoViewGenerator: () -> View,
    invalidator: () -> Unit
) : ItemsListContaner<StudentsGroupTestResultsListItem>(
    context = context,
    idGetter = { it.studentIdentifier },
    producer = producer,
    onEmptyListInfoViewGenerator = onEmptyListInfoViewGenerator,
    invalidator = invalidator,
    viewWrappersCreator = {
        StudentsGroupTestResultsListItemViewWrapper(
            context = context,
            onClick = onClick
        )
    }
)