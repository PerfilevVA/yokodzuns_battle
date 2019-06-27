package com.sibedge.yokodzun.android.layers.test_attempt.tasks.task

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.test_attempt.tasks.task.response.TestAttemptTaskResponseView
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilationTask


class TestAttemptTaskView(
    context: Context,
    task: TestAttemptTasksCompilationTask,
    onResponse: (List<String>) -> Unit
) : LinearLayout(context) {

    init {

        applyVerticalOrientation()
        applyVerticalPadding(SizeManager.DEFAULT_SEPARATION)

        addScrollView {

            isFillViewport = true
            applyLinearParams {
                setStretchedHeight()
                setMatchParentWidth()
                setHorizontalMargins(SizeManager.LARGE_SEPARATION)
            }

            addVerticalLayout {

                addLabel(
                    gravity = HGravity.START_CENTER_VERTICAL,
                    textColor = ColorManager.FG,
                    textSize = SizeManager.TEXT_12,
                    fontType = FontManager.DEFAULT,
                    text = StringGetter(R.string.test_attempt_layer_max_score, task.maxScore.toString())
                ) {
                    applyBottomPadding(SizeManager.SMALL_SEPARATION)
                }

                addLabel(
                    gravity = HGravity.START_CENTER_VERTICAL,
                    textColor = ColorManager.FG,
                    textSize = SizeManager.TEXT_12,
                    fontType = FontManager.DEFAULT,
                    text = task.variant.descriptionMD.toGetter()
                )

            }


        }

        addChild(
            TestAttemptTaskResponseView.create(
                context = context,
                task = task,
                onResponse = onResponse
            )
        ) {
            applyLinearParams {
                setMatchParentWidth()
            }
        }

    }

}