package com.sibedge.yokodzun.android.data.entity

import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.remote_teaching_common.data.test.TestTask
import ru.hnau.remote_teaching_common.data.test.TestTaskVariant


data class TestTaskVariantWithId(
    override val number: Int,
    override val value: TestTaskVariant
) : EntityWithId<TestTaskVariant>