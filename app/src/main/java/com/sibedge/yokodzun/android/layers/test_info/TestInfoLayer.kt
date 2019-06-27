package com.sibedge.yokodzun.android.layers.test_info

import android.content.Context
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.utils.showToast
import ru.hnau.jutils.getter.SuspendGetter
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.not
import ru.hnau.jutils.tryOrElse
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.data.TestsTasksManager
import com.sibedge.yokodzun.android.data.entity.TestTaskWithId
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.test_task_info.TestTaskInfoLayer
import com.sibedge.yokodzun.android.layers.test_task_info.TestTaskInfoLayerCallback
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addMainActonButtonView
import com.sibedge.yokodzun.android.ui.plus_minus.PlusMinusColumnInfo
import com.sibedge.yokodzun.android.utils.extensions.*
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import com.sibedge.yokodzun.android.utils.minusAt
import ru.hnau.remote_teaching_common.data.test.TestSkeleton
import ru.hnau.remote_teaching_common.data.test.TestTask
import ru.hnau.remote_teaching_common.data.test.TestTaskType


class TestInfoLayer(
    context: Context
) : AppLayer(
    context = context
), TestTaskInfoLayerCallback {

    companion object {

        fun newInstance(
            context: Context,
            test: TestSkeleton,
            tasks: List<TestTask>
        ) = TestInfoLayer(context).apply {
            testSkeleton = test
            initialTasks = tasks
            tasksProducer = ActualProducerSimple(tasks)
        }

    }

    @LayerState
    private lateinit var testSkeleton: TestSkeleton

    @LayerState
    private lateinit var initialTasks: List<TestTask>

    @LayerState
    private lateinit var tasksProducer: ActualProducerSimple<List<TestTask>>

    override val title: StringGetter
        get() = StringGetter(R.string.test_info_layer_title, testSkeleton.title)


    private val tasksWithIdAsyncProducer: Producer<GetterAsync<Unit, List<TestTaskWithId>>>
            by lazy {
                tasksProducer.map { tasks ->
                    val tasksWithId = tasks.mapIndexed { i, task -> TestTaskWithId(i, task) }
                    SuspendGetter.simple(tasksWithId)
                } as Producer<GetterAsync<Unit, List<TestTaskWithId>>>
            }

    override fun afterCreate() {
        super.afterCreate()

        content {

            val list = TestTasksContainer(
                context = context,
                onClick = this@TestInfoLayer::onTaskClick,
                onMenuClick = this@TestInfoLayer::onTaskMenuClick,
                producer = tasksWithIdAsyncProducer,
                invalidator = {},
                onEmptyListInfoViewGenerator = {
                    EmptyInfoView(
                        context = context,
                        text = StringGetter(R.string.test_info_layer_no_tasks_title),
                        button = StringGetter(R.string.test_info_layer_no_tasks_button) to this@TestInfoLayer::onAddTaskClick
                    )
                }
            )

            addFrameLayout {

                applyLinearParams {
                    setStretchedHeight()
                    setMatchParentWidth()
                }

                addChild(list)

                addMainActonButtonView(
                    icon = DrawableGetter(R.drawable.ic_add_white),
                    title = StringGetter(R.string.test_info_layer_add_task),
                    needShowTitle = list.onListScrolledToTopProducer.not(),
                    onClick = this@TestInfoLayer::onAddTaskClick
                )

            }

        }

    }

    private fun onTaskClick(task: TestTaskWithId) =
        showLayer(TestTaskInfoLayer.edit(context, task, this))

    private fun updateTasks(editor: (List<TestTask>) -> List<TestTask>) =
        synchronized(this) {
            val newTasks = editor(tasksProducer.currentState)
            tasksProducer.updateState(newTasks)
        }

    override fun onTaskEdited(number: Int, editedTask: TestTask) =
        updateTasks { tasks ->
            tasks.mapIndexed { index, testTask ->
                (index == number).handle(editedTask, testTask)
            }
        }

    override fun onTaskCreated(task: TestTask) =
        updateTasks { tasks -> tasks + task }

    private fun onTaskMenuClick(task: TestTaskWithId) {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.test_info_layer_task_options_title, task.numberUiString))
            closeItem(StringGetter(R.string.test_info_layer_task_options_max_score)) {
                onChangeTaskMaxScoreClick(task)
            }
            closeItem(StringGetter(R.string.dialog_delete)) {
                onDeleteTaskClick(task)
            }
        }
    }

    private fun onChangeTaskMaxScoreClick(task: TestTaskWithId) {
        AppActivityConnector.showPlusMinusDialog(
            title = StringGetter(R.string.test_info_layer_task_edit_max_score_dialog_title),
            text = StringGetter(R.string.test_info_layer_task_edit_max_score_dialog_text),
            initialValue = task.value.maxScore,
            availableValueRange = 1..100,
            valueToStringConverter = { TestTask.maxScoreToUiString(it) },
            columns = listOf(
                PlusMinusColumnInfo(
                    title = "1".toGetter(),
                    actionPlus = { it + 1 },
                    actionMinus = { it - 1 }
                )
            ),
            confirmButtonText = StringGetter(R.string.dialog_save),
            onConfirm = { onTaskEdited(task.number, task.value.copy(maxScore = it)); true }
        )
    }

    private fun onDeleteTaskClick(task: TestTaskWithId) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.test_info_layer_option_delete_task_confirm_dialog_title),
            text = StringGetter(
                R.string.test_info_layer_option_delete_task_confirm_dialog_text,
                task.numberUiString
            ),
            confirmText = StringGetter(R.string.dialog_delete)
        ) {
            updateTasks { it.minusAt(task.number) }
            StringGetter(
                R.string.test_info_layer_option_delete_task_success,
                task.numberUiString
            )
        }
    }

    private fun onAddTaskClick() {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.test_info_layer_add_task))
            closeItem(StringGetter(R.string.test_info_layer_add_task_type_single)) {
                onNewTaskTypeSelected(TestTaskType.SINGLE)
            }
            closeItem(StringGetter(R.string.test_info_layer_add_task_type_multi)) {
                onNewTaskTypeSelected(TestTaskType.MULTI)
            }
            closeItem(StringGetter(R.string.test_info_layer_add_task_type_text)) {
                onNewTaskTypeSelected(TestTaskType.TEXT)
            }
        }
    }

    private fun onNewTaskTypeSelected(type: TestTaskType) =
        showLayer(TestTaskInfoLayer.create(context, type, this))

    override fun handleGoBack(): Boolean {
        uiJobLocked {
            tryOrElse(
                throwsAction = {
                    TestsTasksManager[testSkeleton.uuid].updateTasks(tasksProducer.currentState)
                    managerConnector.goBack()
                },
                onThrow = { error ->
                    ErrorHandler.handle(error)
                    AppActivityConnector.showConfirmDialog(
                        title = StringGetter(R.string.test_info_layer_update_tasks_error_dialog_title),
                        text = StringGetter(R.string.test_info_layer_update_tasks_error_dialog_text),
                        confirmText = StringGetter(R.string.test_info_layer_update_tasks_error_dialog_button_not_save)
                    ) {
                        managerConnector.goBack()
                    }
                }
            )
        }
        return true
    }

}