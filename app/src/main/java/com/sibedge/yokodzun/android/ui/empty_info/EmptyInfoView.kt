package com.sibedge.yokodzun.android.ui.empty_info

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.*
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.button.addRTButton


@SuppressLint("ViewConstructor")
class EmptyInfoView(
    context: Context,
    text: StringGetter,
    button: Pair<StringGetter, () -> Unit>? = null,
    info: EmptyInfoViewInfo = EmptyInfoViewInfo.DEFAULT
) : LinearLayout(context) {

    companion object {

        fun createLoadError(
            context: Context,
            info: EmptyInfoViewInfo = EmptyInfoViewInfo.DEFAULT,
            onButtonClick: () -> Unit
        ) = EmptyInfoView(
            context = context,
            info = info,
            text = StringGetter(R.string.error_load_title),
            button = StringGetter(R.string.error_load_reload) to onButtonClick
        )

    }

    init {
        orientation = VERTICAL
        setPadding(info.paddingHorizontal, info.paddingVertical)
        setCenterForegroundGravity()

        addLabel(
            text = text,
            info = info.title
        )

        button?.let { (title, onClick) ->

            addRTButton(
                text = title,
                info = info.buttonInfo,
                onClick = onClick
            ) {
                setLinearParams(WRAP_CONTENT, WRAP_CONTENT) {
                    setTopMargin(info.titleButtonSeparation.getPxInt(context))
                }
            }

        }

    }

}