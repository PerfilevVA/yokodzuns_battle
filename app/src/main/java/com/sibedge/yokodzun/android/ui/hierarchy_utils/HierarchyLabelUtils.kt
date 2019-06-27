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

fun ViewGroup.addLabelView(
    text: StringGetter,
    info: LabelInfo,
    viewConfigurator: (Label.() -> Unit)? = null
) = addLabel(text, info) {
    setTopPadding(SizeManager.DEFAULT_SEPARATION)
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
        setLeftPadding(SizeManager.DEFAULT_SEPARATION)
        viewConfigurator?.invoke(this)
    }