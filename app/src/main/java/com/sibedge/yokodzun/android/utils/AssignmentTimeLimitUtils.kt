package com.sibedge.yokodzun.android.utils

import ru.hnau.jutils.TimeValue
import ru.hnau.remote_teaching_common.data.test.TestTaskType


object AssignmentTimeLimitUtils {

    private val TIME_TO_READ_SYMBOL_OF_DESCRIPTION = TimeValue.MILLISECOND * 100
    private val TIME_TO_RESOLVE_SIMPLE_TASK = TimeValue.SECOND * 5
    private val TIME_TO_CLICK_BUTTON = TimeValue.SECOND
    private val TIME_TO_TYPE_RESPONSE = TimeValue.SECOND * 5

    fun calc(
        descritpionLength: Int,
        type: TestTaskType,
        maxScore: Int,
        responseOptionsCount: Int
    ): TimeValue {
        val timeToRead = TIME_TO_READ_SYMBOL_OF_DESCRIPTION * descritpionLength
        val timeToResolve = TIME_TO_RESOLVE_SIMPLE_TASK * maxScore
        val timeToResponse = calcTimeToResponse(type, responseOptionsCount)
        return timeToRead + timeToResolve + timeToResponse
    }

    private fun calcTimeToResponse(
        type: TestTaskType,
        responseOptionsCount: Int
    ) = when (type) {
        TestTaskType.SINGLE -> TIME_TO_TYPE_RESPONSE
        TestTaskType.MULTI -> TIME_TO_CLICK_BUTTON * (responseOptionsCount + 1)
        TestTaskType.TEXT -> TIME_TO_CLICK_BUTTON + TIME_TO_TYPE_RESPONSE
    }

}