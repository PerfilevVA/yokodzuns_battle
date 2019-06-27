package com.sibedge.yokodzun.android.layers.main.teacher.groups

import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.utils.showToast
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.students.StudentsOfGroupLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import ru.hnau.remote_teaching_common.data.ActionCodeType
import ru.hnau.remote_teaching_common.data.StudentsGroup


object TeacherGroupsPageUtils {

    data class Action(
        val title: StringGetter,
        val action: () -> Unit
    )

    private fun getUnarchivedStudentsGroupActions(
        studentsGroup: StudentsGroup,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = listOf(

        Action(
            title = StringGetter(R.string.teacher_main_view_groups_options_students),
            action = { AppActivityConnector.showLayer({ StudentsOfGroupLayer.newInstance(it, studentsGroup) }) }
        ),
        Action(
            title = StringGetter(R.string.teacher_main_view_groups_options_archive),
            action = {
                AppActivityConnector.showConfirmDialog(
                    title = StringGetter(R.string.teacher_main_view_groups_option_archive_confirm_dialog_title),
                    text = StringGetter(
                        R.string.teacher_main_view_groups_option_archive_confirm_dialog_text,
                        studentsGroup.name
                    ),
                    confirmText = StringGetter(R.string.teacher_main_view_groups_option_archive_confirm_dialog_button)
                ) {
                    coroutinesExecutor {
                        StudentsGroupsListManager.archive(studentsGroup.name)
                        showToast(
                            StringGetter(
                                R.string.teacher_main_view_groups_options_archive_success,
                                studentsGroup.name
                            )
                        )
                    }

                }
            }
        ),

        Action(
            title = StringGetter(R.string.teacher_main_view_groups_options_generate_register_code),
            action = {
                coroutinesExecutor {
                    val actionCode = StudentsGroupsListManager.generateRegistrationCode(studentsGroup.name)
                    ActionCodeType.CREATE_STUDENT_OF_GROUP.showInfoDialog(actionCode)
                }
            }
        )

    )

    private fun getArchivedStudentsGroupActions(
        studentsGroup: StudentsGroup,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = listOf(

        Action(
            title = StringGetter(R.string.teacher_main_view_groups_options_unarchive),
            action = {
                coroutinesExecutor {
                    StudentsGroupsListManager.unarchive(studentsGroup.name)
                    showToast(
                        StringGetter(
                            R.string.teacher_main_view_groups_options_unarchive_success,
                            studentsGroup.name
                        )
                    )
                }
            }
        ),

        Action(
            title = StringGetter(R.string.dialog_delete),
            action = {
                AppActivityConnector.showConfirmDialog(
                    title = StringGetter(R.string.teacher_main_view_groups_option_delete_confirm_dialog_title),
                    text = StringGetter(
                        R.string.teacher_main_view_groups_option_delete_confirm_dialog_text,
                        studentsGroup.name
                    ),
                    confirmText = StringGetter(R.string.dialog_delete)
                ) {
                    coroutinesExecutor {
                        StudentsGroupsListManager.delete(studentsGroup.name)
                        showToast(
                            StringGetter(
                                R.string.teacher_main_view_groups_options_delete_success,
                                studentsGroup.name
                            )
                        )
                    }

                }
            }
        )

    )

    fun getStudentsGroupActions(
        studentsGroup: StudentsGroup,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) =
        if (studentsGroup.archived) {
            getArchivedStudentsGroupActions(studentsGroup, coroutinesExecutor)
        } else {
            getUnarchivedStudentsGroupActions(studentsGroup, coroutinesExecutor)
        }

}