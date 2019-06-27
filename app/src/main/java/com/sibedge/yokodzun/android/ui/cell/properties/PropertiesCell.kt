package com.sibedge.yokodzun.android.ui.cell.properties

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
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
import com.sibedge.yokodzun.android.ui.cell.label.CellTitle
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


open class PropertiesCell<T : Any>(
    context: Context,
    onClick: ((T) -> Unit)? = null,
    private val activeColor: ColorGetter,
    private val inactiveColor: ColorGetter,
    rippleDrawInfo: RippleDrawInfo,
    private val dataGetter: (T) -> PropertiesCell.Data<T>,
    properties: Collection<PropertiesCellPropertyView.Data<T>>
) : Cell<T>(
    context = context,
    rippleDrawInfo = rippleDrawInfo,
    onClick = onClick
) {

    data class Data<T>(
        val title: StringGetter,
        val active: Boolean = true,
        val additionalActionButtonInfo: CellAdditionalActionButton.Info? = null
    )

    private val titleView = CellTitle(
        context = context,
        activeColor = activeColor,
        inactiveColor = inactiveColor
    )

    private val propertiesViews =
        properties.mapIndexed { index, data ->
            val topMargin = if (index == 0) SizeManager.SMALL_SEPARATION else SizeManager.EXTRA_SMALL_SEPARATION
            PropertiesCellPropertyView(
                context = context,
                data = data
            ).applyLinearParams {
                setMatchParentWidth()
                setTopMargin(topMargin)
            }
        }

    private val mainContentContainer = LinearLayout(context)
        .applyVerticalOrientation()
        .applyLinearParams { setStretchedWidth() }
        .applyStartTopGravity()
        .applyPadding(SizeManager.DEFAULT_SEPARATION, SizeManager.SMALL_SEPARATION)
        .addChild(titleView)
        .apply { propertiesViews.forEach(this::addView) }

    private val additionalActionButton =
        CellAdditionalActionButton(
            context = context,
            rippleDrawInfo = rippleDrawInfo
        )
            .applyLinearParams {
                setMatchParentHeight()
            }

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        addChild(mainContentContainer)
        addChild(additionalActionButton)
    }

    override fun onContentReceived(content: T) {
        val (title, active, additionalActionButtonInfo) =
            dataGetter.invoke(content)

        titleView.info = CellLabel.Info(
            text = title,
            active = active
        )

        additionalActionButton.info = additionalActionButtonInfo
        propertiesViews.forEach { it.item = content }
    }

}
