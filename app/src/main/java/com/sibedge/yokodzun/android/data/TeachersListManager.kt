package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.utils.extensions.sortKey
import com.sibedge.yokodzun.android.utils.minusFirst
import ru.hnau.remote_teaching_common.data.User


object TeachersListManager : RTDataManager<List<User>>() {

    override suspend fun getValue() =
        API.getAllTeachers().await().sortedBy { it.sortKey }

    suspend fun remove(login: String) {
        API.deleteTeacher(login).await()
        updateOrInvalidate { oldValue ->
            oldValue.minusFirst { it.login == login }
        }
    }

    suspend fun generateCreateActionCode() =
        API.generateNewCreateTeacherActionCode().await()

    suspend fun generateRestorePasswordActionCode(login: String) =
        API.generateRestoreTeacherPasswordActionCode(login).await()

}