package com.sibedge.yokodzun.android.utils.extensions

import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.takeIfPositive
import com.sibedge.yokodzun.android.utils.AssignmentTimeLimitUtils
import ru.hnau.remote_teaching_common.data.test.TestSkeleton
import ru.hnau.remote_teaching_common.data.test.TestTask
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilation
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilationTask


val TestAttemptTasksCompilation.assignmentTimeLimit: TimeValue
    get() = tasks.fold(TimeValue.ZERO) { acc, task ->
        val assignmentTimeLimit = AssignmentTimeLimitUtils.calc(
            descritpionLength = task.variant.descriptionMD.length,
            type = task.type,
            maxScore = task.maxScore,
            responseOptionsCount = task.variant.optionsMD.size
        )
        return@fold acc + assignmentTimeLimit
    }

val TestAttemptTasksCompilation.maxScore: Int
    get() = tasks.sumBy(TestAttemptTasksCompilationTask::maxScore)