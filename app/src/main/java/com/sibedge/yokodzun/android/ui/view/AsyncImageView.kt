package com.sibedge.yokodzun.android.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.os.Build.VERSION_CODES.M
import android.view.View
import android.widget.FrameLayout
import com.sibedge.yokodzun.android.utils.ImagesLoader
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.coroutines.createUIJob
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType
import ru.hnau.androidutils.ui.drawables.layout_drawable.view.LayoutDrawableView
import ru.hnau.androidutils.ui.utils.logD
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterColor
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterSize
import ru.hnau.jutils.coroutines.TasksFinalizer
import ru.hnau.jutils.ifFalse
import ru.hnau.jutils.producer.locked_producer.SimpleLockedProducer
import ru.hnau.jutils.tryOrNull


open class AsyncImageView(
    context: Context,
    waiterSize: MaterialWaiterSize,
    color: MaterialWaiterColor = MaterialWaiterColor(
        foreground = ColorManager.PRIMARY,
        background = ColorManager.TRANSPARENT
    )
) : FrameLayout(
    context
) {

    private var currentLoader: (suspend () -> DrawableGetter)? = null

    private val isLocked = SimpleLockedProducer()

    private val isVisibleToUserProducer = createIsVisibleToUserProducer()
    private val uiJob = createUIJob(isVisibleToUserProducer)
    private val tasksFinalizer = TasksFinalizer(uiJob)

    init {
        addView(
            ColorManager.createWaiterView(
                context = context,
                lockedProducer = isLocked,
                size = waiterSize,
                color = color
            )
        )
    }

    fun setImage(
        loader: suspend () -> DrawableGetter
    ) {
        isLocked.setIsLocked(true)
        currentLoader = loader
        tasksFinalizer.finalize {
            val content = tryOrNull { loader() }
            synchronized(this@AsyncImageView) {
                (currentLoader == loader).ifFalse { return@finalize }
                isLocked.setIsLocked(false)
                background = content?.get(context)
            }
        }
    }

}