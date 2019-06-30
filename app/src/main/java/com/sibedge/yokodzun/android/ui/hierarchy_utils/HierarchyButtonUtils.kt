package com.sibedge.yokodzun.android.ui.hierarchy_utils

import android.view.ViewGroup
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.applyTopPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLayoutParams
import com.sibedge.yokodzun.android.ui.view.button.YButton
import com.sibedge.yokodzun.android.ui.view.button.YButtonInfo
import com.sibedge.yokodzun.android.ui.view.button.addYButton
import com.sibedge.yokodzun.android.utils.managers.SizeManager

fun ViewGroup.addButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    info: YButtonInfo,
    viewConfigurator: (YButton.() -> Unit)? = null
) = addYButton(text, onClick, info) {
    applyTopPadding(SizeManager.DEFAULT_SEPARATION)
    applyLayoutParams()
    viewConfigurator?.invoke(this)
}

fun ViewGroup.addLargePrimaryBackgroundShadowButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    viewConfigurator: (YButton.() -> Unit)? = null
) = addButtonView(text, onClick, YButtonInfo.LARGE_PRIMARY_BACKGROUND_SHADOW, viewConfigurator)

fun ViewGroup.addSmallPrimaryTextAndBorderButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    viewConfigurator: (YButton.() -> Unit)? = null
) = addButtonView(text, onClick, YButtonInfo.SMALL_PRIMARY_TEXT_AND_BORDER, viewConfigurator)

fun ViewGroup.addBottomButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    viewConfigurator: (YButton.() -> Unit)? = null
) = addButtonView(text, onClick, YButtonInfo.LARGE_PRIMARY_BACKGROUND_SHADOW) {
    applyLayoutParams { setMatchParentWidth() }
    viewConfigurator?.invoke(this)
}

fun ViewGroup.addSmallFgUnderlineTextButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    viewConfigurator: (YButton.() -> Unit)? = null
) = addButtonView(text, onClick, YButtonInfo.SMALL_FG_TEXT_UNDERLINE, viewConfigurator)
