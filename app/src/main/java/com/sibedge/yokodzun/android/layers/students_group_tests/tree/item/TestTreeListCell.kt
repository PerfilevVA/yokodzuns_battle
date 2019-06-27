package com.sibedge.yokodzun.android.layers.students_group_tests.tree.item

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.dp32
import ru.hnau.androidutils.context_getters.dp_px.dp48
import ru.hnau.androidutils.context_getters.dp_px.dp64
import ru.hnau.androidutils.coroutines.createUIJob
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.utils.getDefaultMeasurement
import ru.hnau.androidutils.ui.view.utils.getMaxMeasurement
import ru.hnau.androidutils.ui.view.utils.makeExactlyMeasureSpec
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterSize
import ru.hnau.jutils.producer.locked_producer.SuspendLockedProducer
import com.sibedge.yokodzun.android.layers.students_group_tests.tree.TestsTreeNode
import com.sibedge.yokodzun.android.ui.cell.Cell
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.label.CellLabel
import com.sibedge.yokodzun.android.ui.cell.label.CellTitle
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import kotlin.coroutines.CoroutineContext


abstract class TestTreeListCell<T : Any>(
    context: Context,
    private val titleResolver: (T) -> StringGetter,
    titleColor: ColorGetter,
    private val testTreeNodeToType: (TestsTreeNode) -> T,
    private val onClick: suspend (TestsTreeNode, T) -> Unit
) : Cell<TestsTreeNode>(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO,
    onClick = {}
) {

    companion object {

        private val PREFERRED_HEIGHT = dp48
        private val LEVEL_OFFSET = SizeManager.DEFAULT_SEPARATION

    }

    private var item: T? = null
    private var node: TestsTreeNode? = null

    private val isVisibleToUserProducer =
        createIsVisibleToUserProducer()

    private val uiJob = createUIJob(
        isVisibleToUserProducer = isVisibleToUserProducer,
        errorsHandler = ErrorHandler
    )

    private val lockedProducer = SuspendLockedProducer()

    private val titleView = CellTitle(
        context = context,
        activeColor = titleColor,
        inactiveColor = titleColor
    )
        .applyHorizontalPadding(SizeManager.DEFAULT_SEPARATION)
        .applyLinearParams { setStretchedWidth() }

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        addChild(titleView)
        addChild(
            ColorManager.createWaiterView(
                context = context,
                lockedProducer = lockedProducer,
                size = MaterialWaiterSize.SMALL
            )
                .applyLinearParams { setSize(PREFERRED_HEIGHT) }
        )
    }

    override fun onClick() {
        super.onClick()
        item?.let { item ->
            node?.let { node ->
                uiJob {
                    lockedProducer {
                        onClick.invoke(node, item)
                    }
                }
            }
        }
    }

    override fun onContentReceived(content: TestsTreeNode) {
        node = content
        val item = testTreeNodeToType(content)
        this.item = item
        titleView.info = CellLabel.Info(titleResolver(item))
        applyStartPadding(LEVEL_OFFSET * content.level)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = getMaxMeasurement(widthMeasureSpec, 0)
        val height = getDefaultMeasurement(heightMeasureSpec, PREFERRED_HEIGHT.getPxInt(context))
        super.onMeasure(
            makeExactlyMeasureSpec(width),
            makeExactlyMeasureSpec(height)
        )
    }

}