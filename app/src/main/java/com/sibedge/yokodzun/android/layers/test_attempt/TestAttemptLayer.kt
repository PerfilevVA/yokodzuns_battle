package com.sibedge.yokodzun.android.layers.test_attempt

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.handle
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.main.MainLayer
import com.sibedge.yokodzun.android.utils.TestAttemptResultUtils
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.closeButton
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilation


class TestAttemptLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            testAttempt: LocalTestAttempt,
            tasksCompilation: TestAttemptTasksCompilation
        ) = TestAttemptLayer(context).apply {
            this.testAttempt = testAttempt
            this.tasksCompilation = tasksCompilation
        }

    }

    @LayerState
    private lateinit var testAttempt: LocalTestAttempt

    @LayerState
    private lateinit var tasksCompilation: TestAttemptTasksCompilation

    private val tasksCompilationProducer: TestAttemptTasksCompilationManager
            by lazy { TestAttemptTasksCompilationManager[testAttempt.uuid] }

    override val title: StringGetter
        get() = StringGetter(R.string.test_attempt_layer_title, testAttempt.testTitle)

    override fun afterCreate() {
        super.afterCreate()

        content {

            addChild(
                TestAttemptLayerView(
                    context = context,
                    tasksCompilation = tasksCompilation,
                    testAttempt = testAttempt,
                    onFinish = this@TestAttemptLayer::onFinish
                )
            ) {
                applyLinearParams {
                    setStretchedHeight()
                    setMatchParentWidth()
                }
            }

        }

    }

    private fun onFinish(
        responses: List<List<String>>
    ) {
        uiJobLocked {
            val score = API.setTestAttemptAnswer(
                testAttemptUuid = testAttempt.uuid,
                answer = responses
            ).await()
            TestsAttemptsForStudentManager.invalidate()
            AppActivityConnector.showLayer(::MainLayer, true)
            showResultDialog(score)
        }
    }

    private fun showResultDialog(
        score: Float
    ) {
        val maxScore = tasksCompilation.maxScore
        val passed = TestAttemptResultUtils.checkIsPassed(
            test = tasksCompilation.test,
            maxScore = maxScore,
            score = score
        )
        AppActivityConnector.showDialog {
            title(StringGetter(R.string.test_attempt_result_dialog_title))
            text(StringGetter { context ->
                listOf(
                    StringGetter(R.string.test_attempt_result_dialog_score) to score.toString().toGetter(),
                    StringGetter(R.string.test_attempt_result_dialog_max_score) to maxScore.toString().toGetter(),
                    StringGetter(R.string.test_attempt_result_dialog_passed) to passed.handle(
                        onTrue = { StringGetter(R.string.dialog_yes) },
                        onFalse = { StringGetter(R.string.dialog_no) }
                    )
                ).joinToString(
                    separator = "\n"
                ) { (key, value) ->
                    "${key.get(context)}: ${value.get(context)}"
                }
            })
            closeButton()
        }
    }

    override fun handleGoBack(): Boolean {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.test_attempt_layer_go_back_warning_dialog_title),
            text = StringGetter(R.string.test_attempt_layer_go_back_warning_dialog_text),
            confirmText = StringGetter(R.string.test_attempt_layer_go_back_warning_dialog_interrupt)
        ) {
            AppActivityConnector.goBack()
        }
        return true
    }

}