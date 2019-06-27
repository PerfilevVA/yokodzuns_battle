package com.sibedge.yokodzun.android.layers.test_attempt.tasks.task.response

import android.content.Context
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyTopPadding
import ru.hnau.jutils.toSingleItemList
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class TestAttemptSingleResponseTaskResponseView(
    context: Context,
    optionsMD: List<String>,
    onResponse: (List<String>) -> Unit
) : TestAttemptTaskResponseView(
    context = context,
    onResponse = onResponse
) {

    init {

        applyTopPadding(SizeManager.DEFAULT_SEPARATION)

        optionsMD.forEachIndexed { i, option ->
            addButton(
                text = option.toGetter(),
                onClick = {
                    response(i.toString().toSingleItemList())
                }
            )
        }

    }

}