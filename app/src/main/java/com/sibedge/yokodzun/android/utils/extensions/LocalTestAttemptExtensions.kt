package com.sibedge.yokodzun.android.utils.extensions

import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.jutils.TimeValue
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.line.LineCell
import ru.hnau.remote_teaching_common.data.StudentsGroup
import ru.hnau.remote_teaching_common.data.test.attempt.TestAttempt
import ru.hnau.remote_teaching_common.utils.uiString


val LocalTestAttempt.sortKey: Long
    get() = outdateTime.milliseconds

val LocalTestAttempt.timeLeftUiString: StringGetter
    get() {
        val timeLeft = timeLeft
        if (timeLeft <= TimeValue.ZERO) {
            return StringGetter(R.string.local_test_attemt_time_left_is_over)
        }
        val formattingLevel = TimeValue.MINUTE.milliseconds
        val timeLeftFormatted = TimeValue(timeLeft.milliseconds / formattingLevel * formattingLevel)
        return StringGetter(R.string.local_test_attemt_time_left, timeLeftFormatted.uiString)
    }

val LocalTestAttempt.lineCellData
    get() = LineCell.Data<LocalTestAttempt>(
        title = testTitle.toGetter(),
        subtitle = timeLeftUiString,
        active = timeLeft > TimeValue.ZERO
    )