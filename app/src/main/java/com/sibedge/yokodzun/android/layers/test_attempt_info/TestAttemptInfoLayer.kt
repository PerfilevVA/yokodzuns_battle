package com.sibedge.yokodzun.android.layers.test_attempt_info

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.TestAttemptTasksCompilationManager
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.addSuspendPresenter
import ru.hnau.remote_teaching_common.data.User
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilation


class TestAttemptInfoLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            student: User,
            testAttempt: LocalTestAttempt
        ) = TestAttemptInfoLayer(context).apply {
            this.student = student
            this.testAttempt = testAttempt
        }

    }

    @LayerState
    private lateinit var student: User

    @LayerState
    private lateinit var testAttempt: LocalTestAttempt

    private val tasksCompilationProducer: TestAttemptTasksCompilationManager
            by lazy { TestAttemptTasksCompilationManager[testAttempt.uuid] }

    override val title: StringGetter
        get() = StringGetter(R.string.test_attempt_info_layer_title, testAttempt.testTitle)

    override fun afterCreate() {
        super.afterCreate()

        content {

            addSuspendPresenter(
                producer = tasksCompilationProducer as Producer<GetterAsync<Unit, TestAttemptTasksCompilation>>,
                invalidator = tasksCompilationProducer::invalidate,
                contentViewGenerator = { tasksCompilation ->
                    TestAttemptInfoLayerView(
                        context = context,
                        tasksCompilation = tasksCompilation,
                        testAttempt = testAttempt
                    )
                }
            ) {
                applyLinearParams {
                    setMatchParentWidth()
                    setStretchedHeight()
                }
            }

        }

    }

}