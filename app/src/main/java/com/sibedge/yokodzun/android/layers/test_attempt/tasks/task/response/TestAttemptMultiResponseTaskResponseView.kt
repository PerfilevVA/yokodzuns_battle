package com.sibedge.yokodzun.android.layers.test_attempt.tasks.task.response

import android.content.Context
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.addVerticalLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import com.sibedge.yokodzun.android.ui.RTCheckBox
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class TestAttemptMultiResponseTaskResponseView(
    context: Context,
    optionsMD: List<String>,
    onResponse: (List<String>) -> Unit
) : TestAttemptTaskResponseView(
    context = context,
    onResponse = onResponse
) {

    init {

        lateinit var checkBoxes: List<RTCheckBox>

        addVerticalLayout {

            applyLinearParams {
                setMatchParentWidth()
                setVerticalMargins(SizeManager.DEFAULT_SEPARATION)
            }

            checkBoxes = optionsMD.mapIndexed { i, option ->
                val checkBox = RTCheckBox(
                    context = context,
                    checked = false,
                    index = i,
                    text = option
                )
                addChild(checkBox)
                checkBox
            }

        }

        addResponseButton {
            checkBoxes
                .filter { it.checked }
                .map { it.index.toString() }
        }

    }

}