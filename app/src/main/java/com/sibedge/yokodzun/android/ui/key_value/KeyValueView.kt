package com.sibedge.yokodzun.android.ui.key_value

import android.content.Context
import android.view.ViewGroup
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class KeyValueView(
    context: Context,
    key: StringGetter,
    value: StringGetter
) : LinearLayout(
    context
) {

    companion object {

        private val LABEL_INFO = LabelInfo(
            textSize = SizeManager.TEXT_12,
            fontType = FontManager.DEFAULT,
            minLines = 1,
            maxLines = 1
        )

    }

    init {
        applyHorizontalOrientation()

        addLabel(
            text = key,
            fontType = FontManager.UBUNTU,
            textSize = SizeManager.TEXT_12,
            textColor = ColorManager.FG_T50,
            gravity = HGravity.START_CENTER_VERTICAL
        ) {
            applyLinearParams {
                setStretchedWidth()
                setEndMargin(SizeManager.DEFAULT_SEPARATION)
            }
        }

        addLabel(
            text = value,
            fontType = FontManager.UBUNTU_BOLD,
            textSize = SizeManager.TEXT_12,
            textColor = ColorManager.PRIMARY,
            gravity = HGravity.END_CENTER_VERTICAL
        ) {
            applyLinearParams()
        }
    }

}

fun <G : ViewGroup> G.addKeyValueView(
    key: StringGetter,
    value: StringGetter,
    viewConfigurator: (KeyValueView.() -> Unit)? = null
) = addChild(
    KeyValueView(context, key, value),
    viewConfigurator
)