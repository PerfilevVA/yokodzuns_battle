package com.sibedge.yokodzun.android.layers.main.teacher

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.jutils.helpers.VariableConnector
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.main.teacher.groups.TeacherGroupsPage
import com.sibedge.yokodzun.android.ui.pager.Pager
import com.sibedge.yokodzun.android.ui.pager.PagerPage


class TeacherContentView(
    context: Context,
    selectedPage: VariableConnector<Int>
) : Pager(
    context = context,
    pages = listOf(
        PagerPage(
            title = StringGetter(R.string.teacher_main_view_groups_page_title),
            viewCreator = { TeacherGroupsPage(context) }
        ),
        PagerPage(
            title = StringGetter(R.string.teacher_main_view_courses_page_title),
            viewCreator = { TeacherCoursesPage(context) }
        )
    ),
    selectedPage = selectedPage
)