package com.sibedge.yokodzun.android.data

import ru.hnau.jutils.cache.AutoCache
import ru.hnau.jutils.ifTrue
import com.sibedge.yokodzun.android.api.API
import ru.hnau.remote_teaching_common.data.test.TestTask
import ru.hnau.remote_teaching_common.data.test.attempt.StudentsGroupTestResults


class TestsStudentsGroupsResultsManager private constructor(
    private val testUUID: String,
    private val studentsGroupName: String
) : RTDataManager<StudentsGroupTestResults>() {

    companion object : AutoCache<Pair<String, String>, TestsStudentsGroupsResultsManager>(
        getter = { (testUUID, studentsGroupName) ->
            TestsStudentsGroupsResultsManager(testUUID, studentsGroupName)
        },
        capacity = 1024
    ) {

        fun get(
            testUUID: String,
            studentsGroupName: String
        ) = get(
            testUUID to studentsGroupName
        )

    }

    override suspend fun getValue() =
        API.getTestStudentsGroupResults(
            testUUID = testUUID,
            studentsGroupName = studentsGroupName
        ).await()

}