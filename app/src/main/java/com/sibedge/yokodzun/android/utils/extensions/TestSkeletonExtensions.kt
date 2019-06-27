package com.sibedge.yokodzun.android.utils.extensions

import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.jutils.TimeValue
import ru.hnau.remote_teaching_common.data.test.TestSkeleton
import ru.hnau.remote_teaching_common.utils.uiString
import kotlin.math.roundToInt


fun TestSkeleton.Companion.passScorePercentageToUiString(
    passScorePercentage: Float
) = "${passScorePercentage.times(100).roundToInt()}%".toGetter()

val TestSkeleton.passScorePercentageUiString: StringGetter
    get() = TestSkeleton.passScorePercentageToUiString(passScorePercentage)

fun TestSkeleton.Companion.timeLimitToUiString(
    timeLimit: Long
) = TimeValue(timeLimit).uiString.toGetter()


val TestSkeleton.timeLimitUiString: StringGetter
    get() = TestSkeleton.timeLimitToUiString(timeLimit)