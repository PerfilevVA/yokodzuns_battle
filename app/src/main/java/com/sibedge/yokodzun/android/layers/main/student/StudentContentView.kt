package com.sibedge.yokodzun.android.layers.main.student

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.jutils.helpers.VariableConnector
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.main.student.tests_attempts.StudentTestsAttemptsPage
import com.sibedge.yokodzun.android.ui.pager.Pager
import com.sibedge.yokodzun.android.ui.pager.PagerPage
import ru.hnau.remote_teaching_common.data.User


class StudentContentView(
    context: Context,
    selectedPage: VariableConnector<Int>,
    student: User
) : Pager(
    context = context,
    pages = listOf(
        PagerPage(
            title = StringGetter(R.string.student_main_view_courses_page_title),
            viewCreator = { StudentCoursesPage(context) }
        ),
        PagerPage(
            title = StringGetter(R.string.student_main_view_tests_attempts_page_title),
            viewCreator = {
                StudentTestsAttemptsPage(
                    context,
                    student
                )
            }
        )
    ),
    selectedPage = selectedPage
)