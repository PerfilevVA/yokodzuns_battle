package com.sibedge.yokodzun.android.layers.students_group_test_results

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.jutils.getter.SuspendGetter
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.utils.TestAttemptResultUtils
import ru.hnau.remote_teaching_common.data.StudentsGroup
import ru.hnau.remote_teaching_common.data.User
import ru.hnau.remote_teaching_common.data.test.TestSkeleton
import ru.hnau.remote_teaching_common.data.test.TestTask
import ru.hnau.remote_teaching_common.data.test.attempt.StudentsGroupTestResults
import ru.hnau.remote_teaching_common.data.test.attempt.tasks_compilation.TestAttemptTasksCompilation


class StudentsGroupTestResultsLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            studentsGroupName: String,
            test: TestSkeleton
        ) = StudentsGroupTestResultsLayer(context).apply {
            this.studentsGroupName = studentsGroupName
            this.test = test
        }

    }

    @LayerState
    private lateinit var studentsGroupName: String

    @LayerState
    private lateinit var test: TestSkeleton

    override val title: StringGetter
        get() = StringGetter(R.string.students_group_test_results_layer_title, test.title)

    private val resultsManager: TestsStudentsGroupsResultsManager by lazy {
        TestsStudentsGroupsResultsManager.get(
            testUUID = test.uuid,
            studentsGroupName = studentsGroupName
        )
    }

    private val studentsOfGroupManager: StudentsOfGroupManager
            by lazy { StudentsOfGroupManager[studentsGroupName] }

    private val tasksManager: TestsTasksManager
            by lazy { TestsTasksManager[test.uuid] }

    private val studentsWithResultsManager: Producer<SuspendGetter<Unit, List<StudentsGroupTestResultsListItem>>> by lazy {
        Producer.combine(
            resultsManager,
            studentsOfGroupManager,
            tasksManager
        ) { resultsAsync, studentsAsync, tasksAsync ->
            SuspendGetter.simple {
                val results = resultsAsync.get()
                val students = studentsAsync.get()
                val maxScore = tasksAsync.get().sumBy(TestTask::maxScore)
                results.results.map { (studentLogin, score) ->
                    val studentIdentifier = students.find { it.login == studentLogin }?.fio ?: studentLogin
                    val passed = TestAttemptResultUtils.checkIsPassed(
                        test = test,
                        score = score,
                        maxScore = maxScore
                    )
                    StudentsGroupTestResultsListItem(
                        studentIdentifier = studentIdentifier,
                        score = score,
                        passed = passed,
                        maxScore = maxScore
                    )
                }.sortedBy { (studentIdentifier, _) ->
                    studentIdentifier
                }
            }
        }
    }

    override fun afterCreate() {
        super.afterCreate()

        content {

            addChild(
                StudentsGroupTestResultsListContainer(
                    context = context,
                    producer = studentsWithResultsManager as Producer<GetterAsync<Unit, List<StudentsGroupTestResultsListItem>>>,
                    onClick = {
                        //TODO
                    },
                    invalidator = this@StudentsGroupTestResultsLayer::invalidateContent,
                    onEmptyListInfoViewGenerator = {
                        EmptyInfoView(
                            context = context,
                            text = StringGetter(R.string.students_group_test_results_no_results_title),
                            button = StringGetter(R.string.error_load_reload) to this@StudentsGroupTestResultsLayer::invalidateContent
                        )
                    }
                )
            )

        }

    }

    private fun invalidateContent() {
        resultsManager.invalidate()
        studentsOfGroupManager.invalidate()
        tasksManager.invalidate()
    }

}