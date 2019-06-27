package com.sibedge.yokodzun.android.data

import ru.hnau.jutils.cache.AutoCache
import ru.hnau.jutils.ifTrue
import com.sibedge.yokodzun.android.api.API
import ru.hnau.remote_teaching_common.data.section.SectionInfo
import ru.hnau.remote_teaching_common.data.test.TestTask


class TestsTasksManager private constructor(
    private val testUUID: String
) : RTDataManager<List<TestTask>>() {

    companion object : AutoCache<String, TestsTasksManager>(
        getter = ::TestsTasksManager,
        capacity = 1024
    )

    override suspend fun getValue() =
        API.getTestTasks(testUUID).await()

    suspend fun updateTasks(tasks: List<TestTask>) {
        (existenceValue == tasks).ifTrue { return }
        API.updateTestTasks(testUUID, tasks).await()
        updateOrInvalidate { tasks }
    }

}