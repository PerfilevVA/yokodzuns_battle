package com.sibedge.yokodzun.android.layers.test_task_info

import android.content.Context
import android.view.View
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.data.entity.TestTaskVariantWithId
import com.sibedge.yokodzun.android.ui.list.base.ItemsListContaner
import ru.hnau.remote_teaching_common.data.test.TestTaskType


class TestTaskVariantsContainer(
    context: Context,
    taskType: TestTaskType,
    producer: Producer<GetterAsync<Unit, List<TestTaskVariantWithId>>>,
    onClick: (TestTaskVariantWithId) -> Unit,
    onMenuClick: (TestTaskVariantWithId) -> Unit,
    onEmptyListInfoViewGenerator: () -> View,
    invalidator: () -> Unit
) : ItemsListContaner<TestTaskVariantWithId>(
    context = context,
    idGetter = TestTaskVariantWithId::number,
    producer = producer,
    viewWrappersCreator = { TestTaskVariantViewWrapper(context, taskType, onClick, onMenuClick) },
    onEmptyListInfoViewGenerator = onEmptyListInfoViewGenerator,
    invalidator = invalidator
)