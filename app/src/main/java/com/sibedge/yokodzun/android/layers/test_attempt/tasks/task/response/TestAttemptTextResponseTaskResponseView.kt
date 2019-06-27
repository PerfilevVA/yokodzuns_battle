package com.sibedge.yokodzun.android.layers.test_attempt.tasks.task.response

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.toSingleItemList
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.ui.input.simple.addSimpleInput
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class TestAttemptTextResponseTaskResponseView(
    context: Context,
    onResponse: (List<String>) -> Unit
) : TestAttemptTaskResponseView(
    context = context,
    onResponse = onResponse
) {

    init {

        val input = SimpleInputView(
            context = context,
            hint = StringGetter(R.string.test_attempt_layer_text_response_hint),
            info = SimpleInputViewInfo.DEFAULT
        )
            .applyLinearParams {
                setMatchParentWidth()
                setHorizontalMargins(SizeManager.LARGE_SEPARATION)
                setVerticalMargins(SizeManager.DEFAULT_SEPARATION)
            }

        addChild(input)

        addResponseButton { input.text.toString().toSingleItemList() }

    }

}