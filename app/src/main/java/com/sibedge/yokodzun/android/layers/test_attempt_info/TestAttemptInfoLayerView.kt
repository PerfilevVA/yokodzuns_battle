package com.sibedge.yokodzun.android.layers.test_attempt_info

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyVerticalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLayoutParams
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.toGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.layers.test_attempt.TestAttemptLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.key_value.addKeyValueView
import com.sibedge.yokodzun.android.utils.extensions.assignmentTimeLimit
import com.sibedge.yokodzun.android.utils.extensions.passScorePercentageUiString
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilation
import ru.hnau.remote_teaching_common.utils.uiString


class TestAttemptInfoLayerView(
    context: Context,
    private val testAttempt: LocalTestAttempt,
    private val tasksCompilation: TestAttemptTasksCompilation
) : LinearLayout(
    context
) {

    init {
        applyVerticalOrientation()
        applyPadding(SizeManager.LARGE_SEPARATION, SizeManager.DEFAULT_SEPARATION)

        mapOf(
            Pair(
                first = StringGetter(R.string.test_attempt_info_layer_info_tasks_count),
                second = tasksCompilation.tasks.size.toString().toGetter()
            ),
            Pair(
                first = StringGetter(R.string.test_attempt_info_layer_info_time_limit),
                second = tasksCompilation.assignmentTimeLimit.uiString.toGetter()
            ),
            Pair(
                first = StringGetter(R.string.test_attempt_info_layer_info_pass_score_percentage),
                second = tasksCompilation.test.passScorePercentageUiString
            )
        ).forEach { (key, value) ->
            addKeyValueView(
                key = key,
                value = value
            ) {
                applyLinearParams {
                    setMatchParentWidth()
                    setVerticalMargins(SizeManager.SMALL_SEPARATION)
                }
            }
        }

        addLinearSeparator()

        addBottomButtonView(
            text = StringGetter(R.string.test_attempt_info_layer_start),
            onClick = this::startAttempt
        )
    }

    private fun startAttempt() {
        AppActivityConnector.showLayer({ context ->
            TestAttemptLayer.newInstance(
                context = context,
                testAttempt = testAttempt,
                tasksCompilation = tasksCompilation
            )
        })
    }

}