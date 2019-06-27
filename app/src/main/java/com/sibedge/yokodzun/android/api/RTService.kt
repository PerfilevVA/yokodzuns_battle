package com.sibedge.yokodzun.android.api

import kotlinx.coroutines.Deferred
import okhttp3.internal.http.HttpHeaders
import retrofit2.http.*
import ru.hnau.jutils.possible.Possible
import ru.hnau.remote_teaching_common.data.StudentsGroup
import ru.hnau.remote_teaching_common.data.User
import ru.hnau.remote_teaching_common.data.UserPermission
import ru.hnau.remote_teaching_common.data.section.SectionContentMDUpdateParam
import ru.hnau.remote_teaching_common.data.section.SectionInfo
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import ru.hnau.remote_teaching_common.data.test.TestSkeleton
import ru.hnau.remote_teaching_common.data.test.TestTask
import ru.hnau.remote_teaching_common.data.test.attempt.StudentsGroupTestResults
import ru.hnau.remote_teaching_common.data.test.attempt.TestAttempt
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilation
import ru.hnau.remote_teaching_common.utils.Validators


interface RTService {

    @PATCH("/client-app-instance/{app-instance-uuid}/push-token/{push-token}")
    fun updatePushToken(
        @Path("push-token") pushToken: String,
        @Path("app-instance-uuid") appInstanceUUID: String
    ): Deferred<Unit>

    @PATCH("/users/{login}/login")
    fun login(
        @Path("login") login: String,
        @Query("password") password: String,
        @Query("app-instance-uuid") appInstanceUUID: String
    ): Deferred<String>

    @PATCH("/users/logout")
    fun logout(
        @Query("from-all-clients") fromAllClients: Boolean,
        @Query("app-instance-uuid") appInstanceUUID: String
    ): Deferred<Unit>

    @GET("/users/me")
    fun me(
    ): Deferred<User>

    @PATCH("/users/me/change-password")
    fun changePassword(
        @Query("new-password") newPassword: String
    ): Deferred<Unit>

    @PATCH("/users/me/change-fio")
    fun changeFIO(
        @Query("new-name") newName: String,
        @Query("new-surname") newSurname: String,
        @Query("new-patronymic") newPatronymic: String
    ): Deferred<Unit>

    @GET("/teachers")
    fun getAllTeachers(
    ): Deferred<List<User>>

    @PUT("/teachers")
    fun generateNewCreateTeacherActionCode(
    ): Deferred<String>

    @PUT("/teachers/{teacher-login}")
    fun registerTeacher(
        @Path("teacher-login") login: String,
        @Query("password") password: String,
        @Query("action-code") actionCode: String
    ): Deferred<Unit>

    @DELETE("/teachers/{teacher-login}")
    fun deleteTeacher(
        @Path("teacher-login") login: String
    ): Deferred<Unit>

    @PATCH("/teachers/{teacher-login}/restore-password-code")
    fun generateRestoreTeacherPasswordActionCode(
        @Path("teacher-login") login: String
    ): Deferred<String>

    @PATCH("/teachers/me/restore-password")
    fun restoreTeacherPassword(
        @Query("new-password") newPassword: String,
        @Query("action-code") actionCode: String
    ): Deferred<String>

    @PUT("/students/{student-login}")
    fun registerStudent(
        @Path("student-login") login: String,
        @Query("password") password: String,
        @Query("action-code") actionCode: String
    ): Deferred<Unit>

    @DELETE("/students/{student-login}")
    fun deleteStudent(
        @Path("student-login") login: String
    ): Deferred<Unit>

    @PATCH("/students/{student-login}/restore-password-code")
    fun getRestoreStudentPasswordActionCode(
        @Path("student-login") login: String
    ): Deferred<String>

    @PATCH("/students/me/restore-password")
    fun restoreStudentPassword(
        @Query("new-password") newPassword: String,
        @Query("action-code") actionCode: String
    ): Deferred<String>

    @GET("/students-groups")
    fun getAllStudentsGroups(
    ): Deferred<List<StudentsGroup>>

    @GET("/students-groups/{students-group-name}/students")
    fun getStudentsOfGroup(
        @Path("students-group-name") studentsGroupName: String
    ): Deferred<List<User>>

    @PUT("/students-groups/{students-group-name}")
    fun createStudentsGroup(
        @Path("students-group-name") studentsGroupName: String
    ): Deferred<Unit>

    @PATCH("/students-groups/{students-group-name}/archive")
    fun archiveStudentsGroup(
        @Path("students-group-name") studentsGroupName: String
    ): Deferred<Unit>

    @PATCH("/students-groups/{students-group-name}/unarchive")
    fun unarchiveStudentsGroup(
        @Path("students-group-name") studentsGroupName: String
    ): Deferred<Unit>

    @DELETE("/students-groups/{students-group-name}")
    fun deleteStudentsGroup(
        @Path("students-group-name") studentsGroupName: String
    ): Deferred<Unit>

    @PUT("/students")
    fun generateStudentsGroupRegistrationCode(
        @Query("students-group-name") studentsGroupName: String
    ): Deferred<String>

    @GET("/sections/{section-uuid}/info")
    fun getSectionInfo(
        @Path("section-uuid") sectionUUID: String
    ): Deferred<SectionInfo>

    @PUT("/sections/{section-uuid}")
    fun addSubsection(
        @Path("section-uuid") sectionUUID: String,
        @Query("title") title: String
    ): Deferred<SectionSkeleton>

    @DELETE("/sections/{section-uuid}")
    fun deleteSection(
        @Path("section-uuid") sectionUUID: String
    ): Deferred<Unit>

    @PATCH("/sections/{section-uuid}/title/{title}")
    fun renameSection(
        @Path("section-uuid") sectionUUID: String,
        @Path("title") title: String
    ): Deferred<Unit>

    @PATCH("/sections/{section-uuid}/content-md")
    fun updateSectionContent(
        @Path("section-uuid") sectionUUID: String,
        @Body contentMDUpdateParam: SectionContentMDUpdateParam
    ): Deferred<Unit>

    @GET("/tests/{test-uuid}/tasks")
    fun getTestTasks(
        @Path("test-uuid") testUUID: String
    ): Deferred<List<TestTask>>

    @PUT("/tests/of-section/{section-uuid}")
    fun addTest(
        @Path("section-uuid") sectionUUID: String,
        @Query("title") title: String
    ): Deferred<TestSkeleton>

    @DELETE("/tests/{test-uuid}")
    fun deleteTest(
        @Path("test-uuid") testUUID: String
    ): Deferred<Unit>

    @PATCH("/tests/{test-uuid}/title/{title}")
    fun updateTestTitle(
        @Path("test-uuid") testUUID: String,
        @Path("title") title: String
    ): Deferred<Unit>

    @PATCH("/tests/{test-uuid}/time-limit/{time-limit}")
    fun updateTestTimeLimit(
        @Path("test-uuid") testUUID: String,
        @Path("time-limit") timeLimit: Long
    ): Deferred<Unit>

    @PATCH("/tests/{test-uuid}/pass-percentage/{pass-percentage}")
    fun updateTestPassPercentage(
        @Path("test-uuid") testUUID: String,
        @Path("pass-percentage") passPercentage: Float
    ): Deferred<Unit>

    @PATCH("/tests/{test-uuid}/tasks")
    fun updateTestTasks(
        @Path("test-uuid") testUUID: String,
        @Body tasks: List<TestTask>
    ): Deferred<Unit>

    @PUT("/tests/{test-uuid}/attempts")
    fun addTestAttempt(
        @Path("test-uuid") testUUID: String,
        @Query("students-group-name") studentsGroupName: String,
        @Query("timeLimit") timeLimit: Long
    ): Deferred<Unit>

    @GET("/tests-attempts/my")
    fun getTestAttempts(
    ): Deferred<List<TestAttempt>>

    @GET("/tests-attempts/{test-attempt-uuid}/tasks-compilation")
    fun getTestAttemptTasksCompilation(
        @Path("test-attempt-uuid") testAttemptUuid: String
    ): Deferred<TestAttemptTasksCompilation>

    @PUT("/tests-attempts/{test-attempt-uuid}/answer")
    fun setTestAttemptAnswer(
        @Path("test-attempt-uuid") testAttemptUuid: String,
        @Body answer: Any//List<List<String>>
    ): Deferred<Float>

    @GET("/tests/{test-uuid}/students-group/{students-group-name}/results")
    fun getTestStudentsGroupResults(
        @Path("test-uuid") testUUID: String,
        @Path("students-group-name") studentsGroupName: String
    ): Deferred<StudentsGroupTestResults>

}