package com.sibedge.yokodzun.android.ui.hierarchy_utils

import android.view.ViewGroup
import com.sibedge.yokodzun.android.ui.button.primary.PrimaryActionButton
import com.sibedge.yokodzun.android.ui.button.primary.PrimaryActionButtonInfo
import com.sibedge.yokodzun.android.ui.button.primary.addPrimaryActionButton
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.buttons.MainActionButton
import ru.hnau.androidutils.ui.view.buttons.addMainActionButton
import ru.hnau.androidutils.ui.view.buttons.main_action.MainActionButtonInfo
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.androidutils.ui.view.utils.setSoftwareRendering
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager

fun <G : ViewGroup> G.addPrimaryActonButtonView(
    icon: DrawableGetter,
    title: StringGetter,
    needShowTitle: Producer<Boolean>,
    onClick: () -> Unit,
    viewConfigurator: (PrimaryActionButton.() -> Unit)? = null
) = addPrimaryActionButton(
    icon = icon,
    onClick = onClick,
    title = title,
    needShowTitle = needShowTitle,
    info = PrimaryActionButtonInfo()
) {
    setSoftwareRendering()
    applyFrameParams {
        setMargins(SizeManager.DEFAULT_SEPARATION)
        setEndBottomGravity()
    }
    viewConfigurator?.invoke(this)
}