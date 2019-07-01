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
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.producer.locked_producer.SuspendLockedProducer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.gradient.YGradientDrawable
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addWaiter
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter.Companion.dp
import ru.hnau.androidutils.ui.view.header.addHeader
import ru.hnau.androidutils.ui.view.utils.apply.*


@SuppressLint("ViewConstructor")
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
        icon = LayoutDrawable.createIndependent(
            context,
            DrawableGetter(R.drawable.ic_options_fg)
        ).toGetter(),
        rippleDrawInfo = ColorManager.FG_ON_TRANSPARENT_RIPPLE_INFO,
        onClick = this::showOptionsMenu
    )

    private val headerTitle: HeaderTitle by lazy {
        HeaderTitle(
            context = context,
            textColor = ColorManager.FG,
            fontType = FontManager.DEFAULT,
            textSize = SizeManager.TEXT_20,
            gravity = HGravity.CENTER
        ).apply {
            if (!showGoBackButton) {
                applyStartPadding(HeaderTitle.DEFAULT_HORIZONTAL_PADDING + Header.DEFAULT_HEIGHT)
            }
        }
    }

    private val topContentContainer: LinearLayout by lazy {
        LinearLayout(context).applyVerticalOrientation()
    }

    private val header: View by lazy {

        LinearLayout(context).apply {

            applyVerticalOrientation()
            applyBackground(YGradientDrawable(context))

            addHeader(
                underStatusBar = true,
                headerBackgroundColor = ColorManager.TRANSPARENT,
                headerHeight = dp(44)
            ) {

                if (showGoBackButton) {
                    addHeaderBackButton(
                        rippleDrawInfo = ColorManager.FG_ON_TRANSPARENT_RIPPLE_INFO,
                        color = ColorManager.FG,
                        onClick = { (context as Activity).onBackPressed() }
                    )
                }

                addChild(headerTitle)
                addChild(headerOptionsMenuButton)

                updateTitle()

            }

            addView(topContentContainer)

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

    protected fun topContent(withTopContent: ViewGroup.() -> Unit) =
        withTopContent(topContentContainer)

}