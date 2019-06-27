package com.sibedge.yokodzun.android.ui.hierarchy_utils

import android.view.ViewGroup
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.MATCH_PARENT
import ru.hnau.androidutils.ui.view.utils.WRAP_CONTENT
import ru.hnau.androidutils.ui.view.utils.apply.applyTopPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLayoutParams
import ru.hnau.androidutils.ui.view.utils.setLayoutParams
import ru.hnau.androidutils.ui.view.utils.setTopPadding
import com.sibedge.yokodzun.android.ui.button.RTButton
import com.sibedge.yokodzun.android.ui.button.RTButtonInfo
import com.sibedge.yokodzun.android.ui.button.addRTButton
import com.sibedge.yokodzun.android.utils.managers.SizeManager

fun ViewGroup.addButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    info: RTButtonInfo,
    viewConfigurator: (RTButton.() -> Unit)? = null
) = addRTButton(text, onClick, info) {
    applyTopPadding(SizeManager.DEFAULT_SEPARATION)
    applyLayoutParams()
    viewConfigurator?.invoke(this)
}

fun ViewGroup.addLargePrimaryBackgroundShadowButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    viewConfigurator: (RTButton.() -> Unit)? = null
) = addButtonView(text, onClick, RTButtonInfo.LARGE_PRIMARY_BACKGROUND_SHADOW, viewConfigurator)

fun ViewGroup.addSmallPrimaryTextAndBorderButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    viewConfigurator: (RTButton.() -> Unit)? = null
) = addButtonView(text, onClick, RTButtonInfo.SMALL_PRIMARY_TEXT_AND_BORDER, viewConfigurator)

fun ViewGroup.addBottomButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    viewConfigurator: (RTButton.() -> Unit)? = null
) = addButtonView(text, onClick, RTButtonInfo.LARGE_PRIMARY_BACKGROUND_SHADOW) {
    applyLayoutParams { setMatchParentWidth() }
    viewConfigurator?.invoke(this)
}

fun ViewGroup.addSmallFgUnderlineTextButtonView(
    text: StringGetter,
    onClick: () -> Unit,
    viewConfigurator: (RTButton.() -> Unit)? = null
) = addButtonView(text, onClick, RTButtonInfo.SMALL_FG_TEXT_UNDERLINE, viewConfigurator)
