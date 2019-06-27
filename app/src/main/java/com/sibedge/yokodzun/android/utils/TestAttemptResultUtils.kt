package com.sibedge.yokodzun.android.utils

import ru.hnau.jutils.takeIfPositive
import ru.hnau.remote_teaching_common.data.test.TestSkeleton


object TestAttemptResultUtils {

    fun checkIsPassed(
        test: TestSkeleton,
        maxScore: Int,
        score: Float
    ): Boolean {
        maxScore.takeIfPositive() ?: return true
        val percentage = score / maxScore.toFloat()
        return percentage >= test.passScorePercentage
    }

}