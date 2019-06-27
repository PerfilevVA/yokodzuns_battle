package com.sibedge.yokodzun.android.layers.test_attempt.tasks.task.response

import android.content.Context
import android.support.v4.view.ViewPager
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.applyVerticalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.button.RTButton
import com.sibedge.yokodzun.android.ui.button.RTButtonInfo
import com.sibedge.yokodzun.android.ui.button.addRTButton
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.remote_teaching_common.data.test.TestTaskType
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilationTask


abstract class TestAttemptTaskResponseView(
    context: Context,
    private val onResponse: (List<String>) -> Unit
) : LinearLayout(
    context
) {

    companion object {

        fun create(
            context: Context,
            task: TestAttemptTasksCompilationTask,
            onResponse: (List<String>) -> Unit
        ) = when (task.type) {
            TestTaskType.SINGLE -> TestAttemptSingleResponseTaskResponseView(
                context = context,
                onResponse = onResponse,
                optionsMD = task.variant.optionsMD
            )
            TestTaskType.MULTI -> TestAttemptMultiResponseTaskResponseView(
                context = context,
                onResponse = onResponse,
                optionsMD = task.variant.optionsMD
            )
            TestTaskType.TEXT -> TestAttemptTextResponseTaskResponseView(
                context = context,
                onResponse = onResponse
            )
        }

    }

    init {
        applyVerticalOrientation()
    }

    protected fun addResponseButton(
        responseCollector: () -> List<String>
    ) = addButton(
        text = StringGetter(R.string.dialog_next),
        onClick = { response(responseCollector()) }
    )

    protected fun addButton(
        text: StringGetter,
        onClick: () -> Unit
    ) = addRTButton(
        text = text,
        onClick = onClick,
        info = RTButtonInfo.LARGE_PRIMARY_BACKGROUND_SHADOW
    ) {
        applyLinearParams {
            setMatchParentWidth()
            setHorizontalMargins(SizeManager.LARGE_SEPARATION)
        }
    }

    protected fun response(response: List<String>) =
        onResponse(response)

}