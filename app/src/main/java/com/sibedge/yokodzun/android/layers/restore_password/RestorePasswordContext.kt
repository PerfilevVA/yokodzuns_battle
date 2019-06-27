package com.sibedge.yokodzun.android.layers.restore_password

import ru.hnau.androidutils.context_getters.StringGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.api.API
import ru.hnau.remote_teaching_common.data.ActionCodeType


sealed class RestorePasswordContext(
    val actionCodeType: ActionCodeType,
    val actionCodeComment: StringGetter
) {

    object Teacher : RestorePasswordContext(
        actionCodeType = ActionCodeType.RESTORE_TEACHER_PASSWORD,
        actionCodeComment = StringGetter(R.string.restore_password_layer_action_code_info_teacher)
    ) {

        override suspend fun restorePassword(actionCode: String, password: String) =
            API.restoreTeacherPassword(password, actionCode).await()

    }

    object Student : RestorePasswordContext(
        actionCodeType = ActionCodeType.RESTORE_STUDENT_PASSWORD,
        actionCodeComment = StringGetter(R.string.restore_password_layer_action_code_info_student)
    ) {

        override suspend fun restorePassword(actionCode: String, password: String) =
            API.restoreStudentPassword(password, actionCode).await()

    }

    abstract suspend fun restorePassword(
        actionCode: String,
        password: String
    ): String

}