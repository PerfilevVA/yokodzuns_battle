package com.sibedge.yokodzun.android.layers.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.coroutines.createUIJob
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutDrawable
import ru.hnau.androidutils.ui.drawables.layout_drawable.createIndependent
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.header.Header
import ru.hnau.androidutils.ui.view.header.HeaderTitle
import ru.hnau.androidutils.ui.view.header.back.button.addHeaderBackButton
import ru.hnau.androidutils.ui.view.header.button.HeaderIconButton
import ru.hnau.androidutils.ui.view.layer.layer.Layer
import ru.hnau.androidutils.ui.view.layer.layer.ManagerConnector
import ru.hnau.androidutils.ui.view.layer.manager.LayerManagerConnector
import ru.hnau.androidutils.ui.view.linear_layout.VerticalLinearLayout
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyBackground
import ru.hnau.androidutils.ui.view.utils.apply.applyStartPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyVerticalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.producer.locked_producer.SuspendLockedProducer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addWaiter
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


@SuppressLint("ViewConstructor")
@Suppress("LeakingThis")
abstract class AppLayer(
    context: Context,
    private val showGoBackButton: Boolean = true
) : LinearLayout(
    context
), Layer {

    override val view = this

    protected abstract val title: StringGetter

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val uiJob = createUIJob(
        isVisibleToUserProducer,
        ErrorHandler
    )

    private val suspendLockedProducer = SuspendLockedProducer()

    @ManagerConnector
    protected lateinit var managerConnector: LayerManagerConnector

    private val headerOptionsMenuButton = HeaderIconButton(
        context = context,
        icon = LayoutDrawable.createIndependent(context, DrawableGetter(R.drawable.ic_options_white)).toGetter(),
        rippleDrawInfo = ColorManager.BG_ON_PRIMARY_RIPPLE_INFO,
        onClick = this::showOptionsMenu
    )

    private val headerTitle: HeaderTitle by lazy {
        HeaderTitle(
            context = context,
            textColor = ColorManager.BG,
            fontType = FontManager.DEFAULT,
            textSize = SizeManager.TEXT_16,
            gravity = HGravity.CENTER
        ).apply {
            if (!showGoBackButton) {
                applyStartPadding(HeaderTitle.DEFAULT_HORIZONTAL_PADDING + Header.DEFAULT_HEIGHT)
            }
        }
    }

    private val header: View by lazy {
        Header(
            context = context,
            underStatusBar = true,
            headerBackgroundColor = ColorManager.PRIMARY
        ).apply {

            if (showGoBackButton) {
                addHeaderBackButton(
                    rippleDrawInfo = ColorManager.BG_ON_PRIMARY_RIPPLE_INFO,
                    color = ColorManager.BG,
                    onClick = { (context as Activity).onBackPressed() }
                )
            }

            addChild(headerTitle)
            addChild(headerOptionsMenuButton)

            updateTitle()
        }
    }

    protected fun updateTitle() {
        headerTitle.text = title
    }

    private fun showOptionsMenu(): Unit =
        AppLayerMenu.show(headerOptionsMenuButton, this::uiJobLocked)

    protected val contentContainer =
        VerticalLinearLayout(context)
            .applyVerticalOrientation()

    private val sceneView = FrameLayout(context)
        .applyBackground(ColorManager.BG)
        .applyLinearParams {
            setMatchParentWidth()
            setStretchedHeight()
        }
        .addChild(contentContainer)
        .addWaiter(suspendLockedProducer)

    protected fun uiJobLocked(action: suspend CoroutineScope.() -> Unit) =
        uiJob { suspendLockedProducer { action.invoke(this) } }

    protected fun showLayer(layer: AppLayer, clearStack: Boolean = false) {
        managerConnector.showLayer(layer, clearStack)
    }

    override fun afterCreate() {
        super.afterCreate()
        applyVerticalOrientation()
        super.addView(header)
        super.addView(sceneView)
    }

    protected fun content(withContent: ViewGroup.() -> Unit) =
        withContent(contentContainer)

}