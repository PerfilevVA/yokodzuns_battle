package com.sibedge.yokodzun.android

import android.content.Context
import android.widget.FrameLayout
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.layers.admin.AdminLayer
import com.sibedge.yokodzun.android.layers.login.LoginLayer
import com.sibedge.yokodzun.android.layers.rater.RaterLayer
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import ru.hnau.androidutils.coroutines.createUIJob
import ru.hnau.androidutils.go_back_handler.GoBackHandler
import ru.hnau.androidutils.ui.view.layer.manager.LayerManager
import ru.hnau.androidutils.ui.view.layer.manager.LayerManagerConnector
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.utils.setFitKeyboard
import ru.hnau.jutils.coroutines.TasksFinalizer
import ru.hnau.jutils.producer.locked_producer.SuspendLockedProducer


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

    private fun getInitialLayer() = when {
        AuthManager.isAdmin -> AdminLayer(context)
        AuthManager.isRater -> RaterLayer(context)
        else -> LoginLayer(context)
    }

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

    override fun handleGoBack() =
        layerManager.handleGoBack()

}