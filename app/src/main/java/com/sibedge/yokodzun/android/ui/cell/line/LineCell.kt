package com.sibedge.yokodzun.android.ui.cell.line

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.dp48
import ru.hnau.androidutils.context_getters.dp_px.dp56
import ru.hnau.androidutils.context_getters.dp_px.dp64
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.clickable.ClickableLayoutDrawableView
import ru.hnau.androidutils.ui.view.header.Header
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.handle
import com.sibedge.yokodzun.android.ui.cell.Cell
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.label.CellLabel
import com.sibedge.yokodzun.android.ui.cell.label.CellSubtitle
import com.sibedge.yokodzun.android.ui.cell.label.CellTitle
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


@SuppressLint("ViewConstructor")
abstract class LineCell<T : Any>(
    context: Context,
    onClick: ((T) -> Unit)? = null,
    private val activeColor: LineCellColor,
    private val inactiveColor: LineCellColor,
    rippleDrawInfo: RippleDrawInfo,
    private val dataGetter: (T) -> Data<T>
) : Cell<T>(
    context,
    rippleDrawInfo,
    onClick
) {

    companion object {

        private val PREFERRED_HEIGHT = dp56

    }

    data class Data<T>(
        val title: StringGetter,
        val subtitle: StringGetter? = null,
        val active: Boolean = true,
        val additionalActionButtonInfo: CellAdditionalActionButton.Info? = null
    )

    private val titleView = CellTitle(
        context = context,
        activeColor = activeColor.titleColor,
        inactiveColor = inactiveColor.titleColor
    )

    private val subtitleView = CellSubtitle(
        context = context,
        activeColor = activeColor.subtitleColor,
        inactiveColor = inactiveColor.subtitleColor
    )
        .applyTopPadding(SizeManager.SMALL_SEPARATION)

    private val titlesContainer = LinearLayout(context)
        .applyVerticalOrientation()
        .applyHorizontalPadding(SizeManager.DEFAULT_SEPARATION)
        .applyCenterGravity()
        .applyLinearParams { setStretchedWidth() }
        .addChild(titleView)
        .addChild(subtitleView)

    private val additionalActionButton =
        CellAdditionalActionButton(
            context = context,
            rippleDrawInfo = rippleDrawInfo
        )

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        addChild(titlesContainer)
        addChild(additionalActionButton)
    }

    override fun onContentReceived(content: T) {
        val (title, subtitle, active, additionalActionButtonInfo) =
            dataGetter.invoke(content)

        titleView.info = CellLabel.Info(
            text = title,
            active = active
        )

        subtitleView.info = CellLabel.Info(
            text = subtitle,
            active = active
        )

        additionalActionButton.info = additionalActionButtonInfo
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