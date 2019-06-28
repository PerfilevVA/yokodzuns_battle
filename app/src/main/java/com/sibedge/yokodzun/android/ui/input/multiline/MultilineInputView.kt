package com.sibedge.yokodzun.android.ui.input.multiline

import android.content.Context
import android.text.InputFilter
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.EditText
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.addView
import ru.hnau.androidutils.ui.view.utils.setPadding


class MultilineInputView(
    context: Context,
    text: StringGetter = StringGetter.EMPTY,
    hint: StringGetter = StringGetter.EMPTY,
    info: MultilineInputViewInfo = MultilineInputViewInfo.DEFAULT
) : EditText(
    context
) {

    init {
        background = null
        setPadding(info.paddingHorizontal, info.paddingVertical)

        gravity = Gravity.TOP or Gravity.START or Gravity.LEFT
        typeface = info.font.get(context).typeface
        setTextColor(info.textColor.get(context))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, info.textSize.getPx(context))
        setText(text.get(context))

        setRawInputType(info.inputType)
        transformationMethod = info.transformationMethod

        setHintTextColor(info.hintTextColor.get(context))
        setHint(hint.get(context))
    }

}

fun <G: ViewGroup> G.addMultilineInput(
    text: StringGetter = StringGetter.EMPTY,
    hint: StringGetter = StringGetter.EMPTY,
    info: MultilineInputViewInfo = MultilineInputViewInfo.DEFAULT,
    viewConfigurator: (MultilineInputView.() -> Unit)? = null
) =
    addChild(
        MultilineInputView(context, text, hint, info),
        viewConfigurator
    )