package com.sibedge.yokodzun.android.layers.raters

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.list.base.BaseListViewWrapper
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class RaterView(
    context: Context,
    private val onRemoveClick: (String) -> Unit
) : LinearLayout(
    context
), BaseListViewWrapper<String> {

    override val view = this

    private val titleView = Label(
        context = context,
        gravity = HGravity.START_CENTER_VERTICAL,
        minLines = 1,
        maxLines = 1,
        fontType = FontManager.BOLD,
        textSize = SizeManager.TEXT_16,
        textColor = ColorManager.FG
    )
        .applyLinearParams { setStretchedWidth() }
        .applyPadding(SizeManager.DEFAULT_SEPARATION)

    private val additionalButton = AdditionalButton<String>(
        context = context,
        additionalButtonInfo = { raterCode ->
            AdditionalButton.Info(
                icon = DrawableGetter(R.drawable.ic_remove_fg),
                action = { onRemoveClick(raterCode) },
                color = ColorManager.RED_TRIPLE
            )
        }
    )
        .applyLinearParams { setMatchParentHeight() }

    init {
        applyHorizontalOrientation()
        addView(titleView)
        addView(additionalButton)
    }

    override fun setContent(content: String, position: Int) {
        titleView.text = content.toGetter()
        additionalButton.data = content
    }

}