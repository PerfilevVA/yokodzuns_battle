package com.sibedge.yokodzun.android.layers.main.teacher.groups

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
import com.sibedge.yokodzun.android.layers.students_group_tests.StudentsGroupTestsLayer
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addMainActonButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addWaiter
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.*
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import ru.hnau.remote_teaching_common.data.StudentsGroup
import ru.hnau.remote_teaching_common.utils.Validators


class TeacherGroupsPage(
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

        val list = StudentsGroupListContainer(
            context = context,
            onClick = this::onStudentsGroupClick,
            onMenuClick = this::onStudentsGroupMenuClick,
            producer = StudentsGroupsListManager as Producer<GetterAsync<Unit, List<StudentsGroup>>>,
            invalidator = StudentsGroupsListManager::invalidate,
            onEmptyListInfoViewGenerator = {
                EmptyInfoView(
                    context = context,
                    text = StringGetter(R.string.teacher_main_view_groups_no_groups_title),
                    button = StringGetter(R.string.teacher_main_view_groups_no_groups_button) to this::onAddGroupClick
                )
            }
        )

        addChild(list)

        addMainActonButtonView(
            icon = DrawableGetter(R.drawable.ic_add_white),
            title = StringGetter(R.string.teacher_main_view_groups_add_group),
            needShowTitle = list.onListScrolledToTopProducer.not(),
            onClick = this::onAddGroupClick
        )

        addWaiter(suspendLockedProducer)

    }

    private fun onStudentsGroupClick(studentsGroup: StudentsGroup) {
        if (studentsGroup.archived) {
            onStudentsGroupMenuClick(studentsGroup)
            return
        }
        AppActivityConnector.showLayer({ StudentsGroupTestsLayer.newInstance(it, studentsGroup) })
    }

    private fun onStudentsGroupMenuClick(studentsGroup: StudentsGroup) {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.teacher_main_view_groups_options_title, studentsGroup.name))

            TeacherGroupsPageUtils.getStudentsGroupActions(
                studentsGroup = studentsGroup,
                coroutinesExecutor = uiJob
            ).forEach { (title, action) ->
                closeItem(title, action)
            }
        }
    }

    private fun onAddGroupClick() {
        AppActivityConnector.showInputDialog(
            title = StringGetter(R.string.teacher_main_view_groups_create_new_dialog_title),
            text = StringGetter(R.string.teacher_main_view_groups_create_new_dialog_text),
            confirmButtonText = StringGetter(R.string.teacher_main_view_groups_create_new_dialog_button),
            inputInfo = SimpleInputViewInfo(
                maxLength = Validators.MAX_STUDENTS_GROUP_NAME_LENGTH
            ),
            onConfirm = this::onNewGroupNameEntered
        )
    }

    private fun onNewGroupNameEntered(groupName: String): Boolean {
        tryOrHandleError {
            Validators.validateStudentsGroupNameOrThrow(groupName)
        } ?: return false

        uiJob {
            suspendLockedProducer {
                StudentsGroupsListManager.createNew(groupName)
                shortToast(StringGetter(R.string.teacher_main_view_groups_create_new_success, groupName))
            }
        }
        return true
    }

}