package com.sibedge.yokodzun.android.layers.main.teacher.groups

import android.content.Context
import android.view.View
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.ui.list.base.ItemCellListContaner
import com.sibedge.yokodzun.android.utils.extensions.getLineCellData
import ru.hnau.remote_teaching_common.data.StudentsGroup


class StudentsGroupListContainer(
    context: Context,
    producer: Producer<GetterAsync<Unit, List<StudentsGroup>>>,
    onClick: (StudentsGroup) -> Unit,
    onEmptyListInfoViewGenerator: () -> View,
    invalidator: () -> Unit,
    onMenuClick: (StudentsGroup) -> Unit
) : ItemCellListContaner<StudentsGroup>(
    context = context,
    idGetter = StudentsGroup::name,
    producer = producer,
    onClick = onClick,
    onEmptyListInfoViewGenerator = onEmptyListInfoViewGenerator,
    cellDataGetter = { it.getLineCellData { onMenuClick(it) } },
    invalidator = invalidator
)