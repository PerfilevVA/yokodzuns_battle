package com.sibedge.yokodzun.android.data

import ru.hnau.jutils.cache.AutoCache
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.utils.minusFirst
import ru.hnau.remote_teaching_common.data.User


class StudentsOfGroupManager private constructor(
        private val studentsGroupName: String
) : RTDataManager<List<User>>() {

    companion object : AutoCache<String, StudentsOfGroupManager>(
            getter = ::StudentsOfGroupManager,
            capacity = 1024
    )

    override suspend fun getValue() =
            API.getStudentsOfGroup(studentsGroupName).await()

    suspend fun remove(login: String) {
        API.deleteStudent(login).await()
        updateOrInvalidate { oldValue ->
            oldValue.minusFirst { it.login == login }
        }
    }

    suspend fun generateRestorePasswordActionCode(login: String) =
            API.getRestoreStudentPasswordActionCode(login).await()



}