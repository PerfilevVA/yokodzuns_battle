package com.sibedge.yokodzun.android.utils.extensions

import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import com.sibedge.yokodzun.android.R
import ru.hnau.remote_teaching_common.data.test.TestTask
import ru.hnau.remote_teaching_common.data.test.TestTaskType
import ru.hnau.remote_teaching_common.data.test.TestTaskVariant


fun TestTask.Companion.maxScoreToUiString(
    maxScore: Int
) = maxScore.toString().toGetter()

val TestTask.maxScoreUiString: StringGetter
    get() = TestTask.maxScoreToUiString(maxScore)

val TestTaskType.uiString: StringGetter
    get() = when (this) {
        TestTaskType.SINGLE -> StringGetter(R.string.test_info_layer_task_item_property_type_single)
        TestTaskType.MULTI -> StringGetter(R.string.test_info_layer_task_item_property_type_multi)
        TestTaskType.TEXT -> StringGetter(R.string.test_info_layer_task_item_property_type_text)
    }

fun TestTask.Companion.typeToUiString(
    type: TestTaskType
) = type.uiString

val TestTask.typeUiString: StringGetter
    get() = TestTask.typeToUiString(type)

fun TestTask.Companion.variantsCountToUiString(
    variants: List<TestTaskVariant>
) = variants.size.toString().toGetter()

val TestTask.variantsCountUiString: StringGetter
    get() = TestTask.variantsCountToUiString(variants)

fun TestTaskVariant.responseToUiString(taskType: TestTaskType) =
    if (taskType == TestTaskType.TEXT) {
        responseParts
    } else {
        responseParts.map { optionsMD[it.toInt()] }
    }.joinToString(", ")