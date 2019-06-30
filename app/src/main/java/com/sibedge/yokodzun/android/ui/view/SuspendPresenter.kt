package com.sibedge.yokodzun.android.ui.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.view_presenter.*
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.locked_producer.LockedProducer
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler

class SuspendPresenter<T : Any>(
    context: Context,
    producer: Producer<GetterAsync<Unit, T>>,
    private val contentViewGenerator: (T) -> View?,
    private val invalidator: () -> Unit
) : SuspendPresenterView<T>(
    context = context,
    producer = producer,
    presenterViewInfo = PresenterViewInfo(
        horizontalSizeInterpolator = SizeInterpolator.MAX,
        verticalSizeInterpolator = SizeInterpolator.MAX
    )
) {

    override fun generateContentView(data: T) =
        contentViewGenerator.invoke(data)
            .toPresentingInfo(ColorManager.DEFAULT_PRESENTING_VIEW_PROPERTIES)

    override fun generateErrorView(error: Throwable): PresentingViewInfo {
        ErrorHandler.handle(error)
        return EmptyInfoView.createLoadError(
            context = context,
            onButtonClick = invalidator
        ).toPresentingInfo(ColorManager.DEFAULT_PRESENTING_VIEW_PROPERTIES)
    }

    override fun generateWaiterView(lockedProducer: LockedProducer) =
        ColorManager.createWaiterView(context, lockedProducer)

}

fun <T : Any, G : ViewGroup> G.addSuspendPresenter(
    producer: Producer<GetterAsync<Unit, T>>,
    contentViewGenerator: (T) -> View,
    invalidator: () -> Unit,
    viewConfigurator: (SuspendPresenter<T>.() -> Unit)? = null
) = addChild(
    SuspendPresenter(
        context,
        producer,
        contentViewGenerator,
        invalidator
    ),
    viewConfigurator
)