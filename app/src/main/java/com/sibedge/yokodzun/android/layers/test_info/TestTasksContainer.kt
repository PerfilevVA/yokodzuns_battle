package com.sibedge.yokodzun.android.layers.test_info

import android.content.Context
import android.view.View
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.data.entity.TestTaskWithId
import com.sibedge.yokodzun.android.ui.list.base.ItemsListContaner


class TestTasksContainer(
    context: Context,
    producer: Producer<GetterAsync<Unit, List<TestTaskWithId>>>,
    onClick: (TestTaskWithId) -> Unit,
    onMenuClick: (TestTaskWithId) -> Unit,
    onEmptyListInfoViewGenerator: () -> View,
    invalidator: () -> Unit
) : ItemsListContaner<TestTaskWithId>(
    context = context,
    idGetter = TestTaskWithId::number,
    producer = producer,
    viewWrappersCreator = { TestTaskViewWrapper(context, onClick, onMenuClick) },
    onEmptyListInfoViewGenerator = onEmptyListInfoViewGenerator,
    invalidator = invalidator
)