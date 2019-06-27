package com.sibedge.yokodzun.android.ui.hierarchy_utils

import android.view.ViewGroup
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

fun <G : ViewGroup> G.addMainActonButtonView(
    icon: DrawableGetter,
    title: StringGetter,
    needShowTitle: Producer<Boolean>,
    onClick: () -> Unit,
    viewConfigurator: (MainActionButton.() -> Unit)? = null
) = addMainActionButton(
    icon = icon,
    onClick = onClick,
    title = title,
    needShowTitle = needShowTitle,
    info = MainActionButtonInfo(
        rippleDrawInfo = ColorManager.BG_ON_PRIMARY_RIPPLE_INFO,
        shadowInfo = ColorManager.DEFAULT_BUTTON_SHADOW_INFO,
        titleLabelInfo = LabelInfo(
            textColor = ColorManager.BG,
            fontType = FontManager.DEFAULT,
            textSize = SizeManager.TEXT_12
        )
    )
) {
    setSoftwareRendering()
    applyFrameParams {
        setMargins(SizeManager.DEFAULT_SEPARATION)
        setEndBottomGravity()
    }
    viewConfigurator?.invoke(this)
}