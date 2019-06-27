package com.sibedge.yokodzun.android.layers.main.admin

import android.content.Context
import android.widget.FrameLayout
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.coroutines.createUIJob
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.utils.shortToast
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.not
import ru.hnau.jutils.producer.locked_producer.SuspendLockedProducer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addMainActonButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addWaiter
import com.sibedge.yokodzun.android.ui.list.UserListContainer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import ru.hnau.remote_teaching_common.data.ActionCodeType
import ru.hnau.remote_teaching_common.data.User


class AdminContentView(
    context: Context
) : FrameLayout(
    context
) {

    private val suspendLockedProducer = SuspendLockedProducer()

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val uiJob = createUIJob(
        isVisibleToUserProducer,
        ErrorHandler
    )

    init {

        val list = UserListContainer(
            context = context,
            onClick = this::onTeacherClick,
            producer = TeachersListManager as Producer<GetterAsync<Unit, List<User>>>,
            invalidator = TeachersListManager::invalidate,
            onEmptyListInfoViewGenerator = {
                EmptyInfoView(
                    context = context,
                    text = StringGetter(R.string.admin_main_view_no_teachers_title),
                    button = StringGetter(R.string.admin_main_view_no_teachers_button) to this::generateTeacherRegisterActionCode
                )
            }
        )

        addChild(list)

        addMainActonButtonView(
            icon = DrawableGetter(R.drawable.ic_add_white),
            title = StringGetter(R.string.admin_main_view_add_teacher),
            needShowTitle = list.onListScrolledToTopProducer.not(),
            onClick = this::generateTeacherRegisterActionCode
        )

        addWaiter(suspendLockedProducer)

    }

    private fun onTeacherClick(teacher: User) {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.admin_main_view_teacher_options_title, teacher.fioOrLogin))

            closeItem(
                StringGetter(R.string.admin_main_view_teacher_options_restore_password)
            ) { onTeacherRestorePasswordClick(teacher) }

            closeItem(
                StringGetter(R.string.dialog_delete)
            ) { onTeacherRemoveClick(teacher) }
        }
    }

    private fun onTeacherRestorePasswordClick(teacher: User) {
        uiJob {
            val actionCode = suspendLockedProducer {
                TeachersListManager.generateRestorePasswordActionCode(teacher.login)
            }
            ActionCodeType.RESTORE_TEACHER_PASSWORD.showInfoDialog(actionCode)
        }
    }

    private fun onTeacherRemoveClick(teacher: User) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.admin_main_view_remove_teacher_confirm_title),
            text = StringGetter(R.string.admin_main_view_remove_teacher_confirm_text, teacher.fioOrLogin),
            confirmText = StringGetter(R.string.dialog_delete)
        ) {
            uiJob {
                suspendLockedProducer {
                    TeachersListManager.remove(teacher.login)
                    shortToast(StringGetter(R.string.admin_main_view_remove_teacher_success, teacher.fioOrLogin))
                }
            }
        }
    }

    private fun generateTeacherRegisterActionCode() {
        uiJob {
            val actionCode = suspendLockedProducer {
                TeachersListManager.generateCreateActionCode()
            }
            ActionCodeType.CREATE_TEACHER.showInfoDialog(actionCode)
        }
    }


}