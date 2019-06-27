package com.sibedge.yokodzun.android.utils.extensions

import ru.hnau.androidutils.context_getters.StringGetter
import com.sibedge.yokodzun.android.R
import ru.hnau.remote_teaching_common.data.UserRole



private val USER_ROLE_TITLES = hashMapOf(
    UserRole.STUDENT to StringGetter(R.string.user_role_student),
    UserRole.TEACHER to StringGetter(R.string.user_role_teacher)
)

val UserRole.title: StringGetter
    get() = USER_ROLE_TITLES[this] ?: StringGetter.EMPTY


