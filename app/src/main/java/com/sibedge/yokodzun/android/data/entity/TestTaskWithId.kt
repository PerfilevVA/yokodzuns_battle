package com.sibedge.yokodzun.android.data.entity

import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.remote_teaching_common.data.test.TestTask


data class TestTaskWithId(
    override val number: Int,
    override val value: TestTask
) : EntityWithId<TestTask>