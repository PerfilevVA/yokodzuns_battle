package com.sibedge.yokodzun.android.layers.test_attempt.tasks

import android.content.Context
import android.view.View
import ru.hnau.androidutils.ui.utils.Side
import ru.hnau.androidutils.ui.view.view_presenter.*
import ru.hnau.jutils.helpers.toBox
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.layers.test_attempt.tasks.task.TestAttemptTaskView
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilation
import java.util.*


class TestAttemptTasksView private constructor(
    context: Context,
    tasksViews: Producer<out View>,
    val progress: Producer<Int>,
    val responses: List<List<String>>
) : PresenterView(
    context = context,
    presentingViewProducer = tasksViews.map { it.toPresentingInfo(PRESENTING_VIEW_PROPERTIES) }
) {

    companion object {

        private val PRESENTING_VIEW_PROPERTIES = PresentingViewProperties(
            fromSide = Side.END
        )

        fun create(
            context: Context,
            tasksCompilation: TestAttemptTasksCompilation
        ): TestAttemptTasksView {

            val taskNumberProducer =
                ActualProducerSimple(0)

            val tasksProducer =
                taskNumberProducer.map { taskNumber ->
                    tasksCompilation.tasks.getOrNull(taskNumber).toBox()
                }

            val responses = LinkedList<List<String>>()

            val tasksViewsProducer = tasksProducer.mapNotNull { taskBox ->
                val task = taskBox.value ?: return@mapNotNull null
                TestAttemptTaskView(
                    context = context,
                    task = task
                ) { response ->
                    if (taskNumberProducer.currentState < tasksCompilation.tasks.size) {
                        responses += response
                    }
                    taskNumberProducer.updateState(taskNumberProducer.currentState + 1)
                }
            }

            return TestAttemptTasksView(
                context = context,
                progress = taskNumberProducer,
                tasksViews = tasksViewsProducer,
                responses = responses
            )

        }

    }

}