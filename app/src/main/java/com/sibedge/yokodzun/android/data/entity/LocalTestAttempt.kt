package com.sibedge.yokodzun.android.data.entity

import ru.hnau.jutils.TimeValue
import ru.hnau.remote_teaching_common.data.test.attempt.TestAttempt


class LocalTestAttempt(
    val uuid: String,
    val testUUID: String,
    val testTitle: String,
    val studentsGroupName: String,
    val outdateTime: TimeValue
) {

    constructor(
        testAttempt: TestAttempt
    ) : this(
        uuid = testAttempt.uuid,
        testUUID = testAttempt.testUUID,
        testTitle = testAttempt.testTitle,
        studentsGroupName = testAttempt.studentsGroupName,
        outdateTime = TimeValue.now() + TimeValue(testAttempt.timeLeft)
    )

    val timeLeft: TimeValue
        get() = outdateTime - TimeValue.now()

}