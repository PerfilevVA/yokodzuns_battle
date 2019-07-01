package com.sibedge.yokodzun.android.ui.hierarchy_utils

import android.view.ViewGroup
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.setLeftPadding
import ru.hnau.androidutils.ui.view.utils.setTopPadding
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.androidutils.ui.view.utils.apply.applyStartPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyTopPadding

fun ViewGroup.addLabelView(
    text: StringGetter,
    info: LabelInfo,
    viewConfigurator: (Label.() -> Unit)? = null
) = addLabel(text, info) {
    applyTopPadding(SizeManager.DEFAULT_SEPARATION)
    viewConfigurator?.invoke(this)
}

fun ViewGroup.addFgSmallLabelView(
    text: StringGetter,
    viewConfigurator: (Label.() -> Unit)? = null
) =
    addLabelView(text, ColorManager.FG_SMALL_LABEL_INFO, viewConfigurator)

fun ViewGroup.addFgSmallInputLabelView(
    text: StringGetter,
    viewConfigurator: (Label.() -> Unit)? = null
) =
    addLabelView(text, ColorManager.FG_SMALL_LABEL_INFO) {
        applyStartPadding(SizeManager.DEFAULT_SEPARATION)
        viewConfigurator?.invoke(this)
    }