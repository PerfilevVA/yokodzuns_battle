package com.sibedge.yokodzun.android.layers.students_group_tests

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.utils.longToast
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.students_group_test_results.StudentsGroupTestResultsLayer
import com.sibedge.yokodzun.android.layers.students_group_tests.tree.TestsTreeList
import com.sibedge.yokodzun.android.ui.addSuspendPresenter
import com.sibedge.yokodzun.android.ui.plus_minus.PlusMinusColumnInfo
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import ru.hnau.remote_teaching_common.data.StudentsGroup
import ru.hnau.remote_teaching_common.data.section.SectionInfo
import ru.hnau.remote_teaching_common.data.test.TestSkeleton
import ru.hnau.remote_teaching_common.utils.Validators


class StudentsGroupTestsLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            studentsGroup: StudentsGroup
        ) = StudentsGroupTestsLayer(context).apply {
            this.studentsGroup = studentsGroup
        }

    }

    @LayerState
    private lateinit var studentsGroup: StudentsGroup

    override val title: StringGetter
        get() = StringGetter(R.string.students_group_tests_layer_title, studentsGroup.name)

    override fun afterCreate() {
        super.afterCreate()

        content {

            addSuspendPresenter(
                producer = SectionsInfoManager.COURSES as Producer<GetterAsync<Unit, SectionInfo>>,
                invalidator = SectionsInfoManager.COURSES::invalidate,
                contentViewGenerator = {
                    TestsTreeList(
                        context,
                        it.subsections,
                        this@StudentsGroupTestsLayer::onTestClick
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

    private fun onTestClick(testSkeleton: TestSkeleton) {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.students_group_tests_layer_test_options_title, testSkeleton.title))
            closeItem(StringGetter(R.string.students_group_tests_layer_test_options_add_attempt)) {
                onAddAttemptClick(testSkeleton)
            }
            closeItem(StringGetter(R.string.students_group_tests_layer_test_options_results)) {
                AppActivityConnector.showLayer({ context ->
                    StudentsGroupTestResultsLayer.newInstance(
                        context = context,
                        studentsGroupName = studentsGroup.name,
                        test = testSkeleton
                    )
                })
            }
        }
    }

    private fun onAddAttemptClick(testSkeleton: TestSkeleton) {
        AppActivityConnector.showPlusMinusDialog<TimeValue>(
            title = StringGetter(R.string.students_group_tests_layer_add_attempt_time_limit_dialog_title),
            text = StringGetter(R.string.students_group_tests_layer_add_attempt_time_limit_dialog_text),
            initialValue = TimeValue(testSkeleton.timeLimit),
            availableValueRange = Validators.TEST_MIN_TIME_LIMIT..Validators.TEST_MAX_TIME_LIMIT,
            valueToStringConverter = { TestSkeleton.timeLimitToUiString(it.milliseconds) },
            columns = listOf(
                PlusMinusColumnInfo(
                    title = StringGetter(R.string.plus_minus_column_title_day),
                    actionPlus = { it + TimeValue.DAY },
                    actionMinus = { it - TimeValue.DAY }
                ),
                PlusMinusColumnInfo(
                    title = StringGetter(R.string.plus_minus_column_title_hour),
                    actionPlus = { it + TimeValue.HOUR },
                    actionMinus = { it - TimeValue.HOUR }
                ),
                PlusMinusColumnInfo(
                    title = StringGetter(R.string.plus_minus_column_title_minute),
                    actionPlus = { it + TimeValue.MINUTE },
                    actionMinus = { it - TimeValue.MINUTE }
                )
            ),
            confirmButtonText = StringGetter(R.string.dialog_add),
            onConfirm = { onTimeLimitEntered(testSkeleton, it); true }
        )
    }

    private fun onTimeLimitEntered(testSkeleton: TestSkeleton, timeLimit: TimeValue) {
        uiJobLocked {
            API.addTestAttempt(
                testUUID = testSkeleton.uuid,
                timeLimit = timeLimit.milliseconds,
                studentsGroupName = studentsGroup.name
            ).await()
            longToast(
                StringGetter(
                    R.string.students_group_tests_layer_attempt_added,
                    testSkeleton.title,
                    studentsGroup.name
                )
            )
        }
    }

}