package com.sibedge.yokodzun.android.layers.test_attempt

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyVerticalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.utils.ContextConnector.context
import ru.hnau.androidutils.utils.handler.HandlerWaiter
import ru.hnau.jutils.producer.extensions.int.plus
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.layers.test_attempt.tasks.TestAttemptTasksView
import com.sibedge.yokodzun.android.utils.extensions.assignmentTimeLimit
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilation


class TestAttemptLayerView(
    context: Context,
    testAttempt: LocalTestAttempt,
    tasksCompilation: TestAttemptTasksCompilation,
    onFinish: (responses: List<List<String>>) -> Unit
) : LinearLayout(
    context
) {

    private lateinit var finishWaiter: HandlerWaiter

    init {
        applyVerticalOrientation()

        val tasksView =
            TestAttemptTasksView.create(
                context = context,
                tasksCompilation = tasksCompilation
            )

        val progress = tasksView.progress
        val tasksCount = tasksCompilation.tasks.size
        val timeLeft = tasksCompilation.assignmentTimeLimit

        addChild(
            TestAttemptTimeLeftView(
                context = context,
                timeLeft = timeLeft
            )
        ) {
            applyLinearParams {
                setTopMargin(SizeManager.DEFAULT_SEPARATION)
                setMatchParentWidth()
            }
        }

        addChild(
            TestAttemptProgressView(
                context = context,
                progress = progress + 1,
                max = tasksCount
            )
        ) {
            applyLinearParams {
                setVerticalMargins(SizeManager.SMALL_SEPARATION)
                setMatchParentWidth()
            }
        }

        addChild(tasksView) {
            applyLinearParams {
                setMatchParentWidth()
                setStretchedHeight()
            }
        }

        progress.attach { progressValue ->
            if (progressValue >= tasksCount) {
                finishWaiter.cancel()
                onFinish(tasksView.responses)
            }
        }

        finishWaiter = HandlerWaiter { onFinish(tasksView.responses) }
            .apply { start(timeLeft) }

    }

}