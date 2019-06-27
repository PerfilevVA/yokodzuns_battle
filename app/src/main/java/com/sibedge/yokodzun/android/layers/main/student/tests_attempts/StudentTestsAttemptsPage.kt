package com.sibedge.yokodzun.android.layers.main.student.tests_attempts

import android.content.Context
import android.widget.FrameLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.coroutines.createUIJob
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.locked_producer.SuspendLockedProducer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.entity.LocalTestAttempt
import com.sibedge.yokodzun.android.layers.test_attempt_info.TestAttemptInfoLayer
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addWaiter
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import ru.hnau.remote_teaching_common.data.User


class StudentTestsAttemptsPage(
    context: Context,
    private val student: User
) : FrameLayout(
    context
) {

    private val suspendLockedProducer =
        SuspendLockedProducer()

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val uiJob = createUIJob(
        isVisibleToUserProducer,
        ErrorHandler
    )

    init {

        addChild(
            TestAttemptListItemContainer(
                context = context,
                onClick = this::onAttemptClick,
                producer = TestsAttemptsForStudentManager as Producer<GetterAsync<Unit, List<LocalTestAttempt>>>,
                invalidator = TestsAttemptsForStudentManager::invalidate,
                onEmptyListInfoViewGenerator = {
                    EmptyInfoView(
                        context = context,
                        text = StringGetter(R.string.student_main_view_tests_attempts_page_no_attempts)
                    )
                }
            )
        )

        addWaiter(suspendLockedProducer)

    }

    private fun onAttemptClick(attempt: LocalTestAttempt) {
        AppActivityConnector.showLayer({ context -> TestAttemptInfoLayer.newInstance(context, student, attempt) })
    }

}