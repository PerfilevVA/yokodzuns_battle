package com.sibedge.yokodzun.android.layers.students

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.utils.shortToast
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import ru.hnau.remote_teaching_common.data.ActionCodeType
import ru.hnau.remote_teaching_common.data.StudentsGroup
import ru.hnau.remote_teaching_common.data.User


class StudentsOfGroupLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            studentsGroup: StudentsGroup
        ) = StudentsOfGroupLayer(context).apply {
            this.studentsGroup = studentsGroup
        }

    }

    @LayerState
    private lateinit var studentsGroup: StudentsGroup

    override val title: StringGetter
        get() = StringGetter(R.string.students_of_group_layer_title, studentsGroup.name)

    private val studentsOfGroupManager: StudentsOfGroupManager by lazy {
        StudentsOfGroupManager[studentsGroup.name]
    }

    override fun afterCreate() {
        super.afterCreate()

        content {

            addChild(
                UserListContainer(
                    context = context,
                    onClick = this@StudentsOfGroupLayer::onStudentClick,
                    producer = studentsOfGroupManager as Producer<GetterAsync<Unit, List<User>>>,
                    invalidator = studentsOfGroupManager::invalidate,
                    onEmptyListInfoViewGenerator = {
                        EmptyInfoView(
                            context = context,
                            text = StringGetter(R.string.students_of_group_layer_no_students_title),
                            button = StringGetter(R.string.students_of_group_layer_no_students_button) to this@StudentsOfGroupLayer::generateStudentsRegisterActionCode
                        )
                    }
                )
            )

        }

    }

    private fun generateStudentsRegisterActionCode() {
        uiJobLocked {
            val actionCode = StudentsGroupsListManager.generateRegistrationCode(studentsGroup.name)
            ActionCodeType.CREATE_STUDENT_OF_GROUP.showInfoDialog(actionCode)
        }
    }

    private fun onStudentClick(student: User) {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.students_of_group_layer_options_title, student.fioOrLogin))

            closeItem(
                StringGetter(R.string.students_of_group_layer_options_restore_password)
            ) { onStudentRestorePasswordClick(student) }

            closeItem(
                StringGetter(R.string.dialog_delete)
            ) { onStudentRemoveClick(student) }
        }
    }

    private fun onStudentRestorePasswordClick(student: User) {
        uiJobLocked {
            val actionCode = studentsOfGroupManager.generateRestorePasswordActionCode(student.login)
            ActionCodeType.RESTORE_STUDENT_PASSWORD.showInfoDialog(actionCode)
        }
    }

    private fun onStudentRemoveClick(student: User) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.students_of_group_layer_remove_student_confirm_title),
            text = StringGetter(R.string.students_of_group_layer_remove_student_confirm_text, student.fioOrLogin),
            confirmText = StringGetter(R.string.dialog_delete)
        ) {
            uiJobLocked {
                studentsOfGroupManager.remove(student.login)
                shortToast(
                    StringGetter(
                        R.string.students_of_group_layer_remove_student_success,
                        student.fioOrLogin
                    )
                )
            }
        }
    }

}