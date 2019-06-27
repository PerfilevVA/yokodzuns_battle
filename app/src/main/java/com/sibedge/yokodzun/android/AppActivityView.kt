package com.sibedge.yokodzun.android

import android.content.Context
import android.widget.FrameLayout
import ru.hnau.androidutils.coroutines.createUIJob
import ru.hnau.androidutils.go_back_handler.GoBackHandler
import ru.hnau.androidutils.ui.view.layer.manager.LayerManager
import ru.hnau.androidutils.ui.view.layer.manager.LayerManagerConnector
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.utils.setFitKeyboard
import ru.hnau.jutils.coroutines.TasksFinalizer
import ru.hnau.jutils.producer.locked_producer.SuspendLockedProducer
import com.sibedge.yokodzun.android.layers.LoginLayer
import com.sibedge.yokodzun.android.layers.main.MainLayer
import com.sibedge.yokodzun.android.layers.test_attempt_info.TestAttemptInfoLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler


class AppActivityView(
    context: Context
) : FrameLayout(
    context
), GoBackHandler {

    private val layerManager: LayerManager by lazy {
        val layerManager = LayerManager(context).apply {
            showLayer(getInitialLayer())
            setFitKeyboard()
        }
        return@lazy layerManager
    }

    val layerManagerConnector: LayerManagerConnector
        get() = layerManager

    private fun getInitialLayer() =
        if (AuthManager.logged) MainLayer(context) else LoginLayer(context)

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val uiJob = createUIJob(
        isVisibleToUserProducer,
        ErrorHandler
    )

    private val tasksFinalizer =
        TasksFinalizer(uiJob)

    private val suspendLockedProducer = SuspendLockedProducer()

    init {
        addView(layerManager)
        addView(ColorManager.createWaiterView(context, suspendLockedProducer))
    }

    fun startTestAttempt(
        testAttempUUID: String
    ) {
        tasksFinalizer.finalize {
            suspendLockedProducer {
                val testAttempt = TestsAttemptsForStudentManager.wait().get()
                    .find { it.uuid == testAttempUUID } ?: return@suspendLockedProducer
                val me = MeInfoManager.wait().get()
                AppActivityConnector.showLayer({ TestAttemptInfoLayer.newInstance(context, me, testAttempt) })
            }
        }
    }

    override fun handleGoBack() =
            layerManager.handleGoBack()

}