package com.sibedge.yokodzun.android.data

import ru.hnau.jutils.cache.AutoCache
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.utils.extensions.sortKey
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilation


class TestAttemptTasksCompilationManager private constructor(
    private val testAttemptUUID: String
) : RTDataManager<TestAttemptTasksCompilation>() {

    companion object : AutoCache<String, TestAttemptTasksCompilationManager>(
        getter = ::TestAttemptTasksCompilationManager,
        capacity = 1024
    )

    override suspend fun getValue() = API
        .getTestAttemptTasksCompilation(testAttemptUUID)
        .await()

}