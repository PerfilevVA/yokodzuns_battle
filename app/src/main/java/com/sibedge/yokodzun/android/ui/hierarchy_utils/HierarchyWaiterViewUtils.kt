package com.sibedge.yokodzun.android.ui.hierarchy_utils

import android.view.ViewGroup
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.waiter.WaiterView
import ru.hnau.jutils.producer.locked_producer.LockedProducer
import com.sibedge.yokodzun.android.utils.managers.ColorManager

fun ViewGroup.addWaiter(
    lockedProducer: LockedProducer,
    viewConfigurator: (WaiterView.() -> Unit)? = null
) = addChild(
    ColorManager.createWaiterView(context, lockedProducer).apply {
        viewConfigurator?.let(this::apply)
    }
)