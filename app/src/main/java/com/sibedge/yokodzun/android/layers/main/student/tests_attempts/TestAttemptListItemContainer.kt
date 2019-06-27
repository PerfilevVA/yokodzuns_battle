package com.sibedge.yokodzun.android.layers.main.student.tests_attempts

import android.content.Context
import android.view.View
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.ui.list.base.ItemCellListContaner
import com.sibedge.yokodzun.android.utils.extensions.getLineCellData
import com.sibedge.yokodzun.android.utils.extensions.lineCellData
import ru.hnau.remote_teaching_common.data.StudentsGroup
import ru.hnau.remote_teaching_common.data.test.attempt.TestAttempt


class TestAttemptListItemContainer(
    context: Context,
    producer: Producer<GetterAsync<Unit, List<LocalTestAttempt>>>,
    onClick: (LocalTestAttempt) -> Unit,
    onEmptyListInfoViewGenerator: () -> View,
    invalidator: () -> Unit
) : ItemCellListContaner<LocalTestAttempt>(
    context = context,
    idGetter = LocalTestAttempt::uuid,
    producer = producer,
    onClick = onClick,
    onEmptyListInfoViewGenerator = onEmptyListInfoViewGenerator,
    cellDataGetter = { it.lineCellData },
    invalidator = invalidator
)