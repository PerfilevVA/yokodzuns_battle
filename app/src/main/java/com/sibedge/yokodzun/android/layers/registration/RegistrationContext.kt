package com.sibedge.yokodzun.android.layers.registration

import ru.hnau.androidutils.context_getters.StringGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.api.API
import ru.hnau.remote_teaching_common.data.ActionCodeType


sealed class RegistrationContext(
    val actionCodeType: ActionCodeType,
    val actionCodeComment: StringGetter
) {

    object Teacher : RegistrationContext(
        actionCodeType = ActionCodeType.CREATE_TEACHER,
        actionCodeComment = StringGetter(R.string.registration_layer_action_code_info_teacher)
    ) {

        override suspend fun register(actionCode: String, login: String, password: String) =
            API.registerTeacher(login, password, actionCode).await()

    }

    object Student : RegistrationContext(
        actionCodeType = ActionCodeType.CREATE_STUDENT_OF_GROUP,
        actionCodeComment = StringGetter(R.string.registration_layer_action_code_info_student)
    ) {

        override suspend fun register(actionCode: String, login: String, password: String) =
            API.registerStudent(login, password, actionCode).await()

    }

    abstract suspend fun register(
        actionCode: String,
        login: String,
        password: String
    )

}