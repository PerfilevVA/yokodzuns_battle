package com.sibedge.yokodzun.android.ui.view.input.simple


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.text.InputFilter
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.EditText
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.bounds_producer.ViewBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RoundSidesRectCanvasShape
import ru.hnau.androidutils.ui.drawer.Insets
import ru.hnau.androidutils.ui.drawer.border.BorderDrawer
import ru.hnau.androidutils.ui.utils.types_utils.doInState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.setPadding


@SuppressLint("ViewConstructor")
class SimpleInputView(
    context: Context,
    text: StringGetter = StringGetter.EMPTY,
    hint: StringGetter = StringGetter.EMPTY,
    info: SimpleInputViewInfo = SimpleInputViewInfo.DEFAULT
) : EditText(
    context
) {

    private val insets =
        info.border.insets + Insets(info.borderMargin)

    private val boundsProducer =
        ViewBoundsProducer(
            view = this,
            usePaddings = false
        ).applyInsents(context, insets)

    private val canvasShape =
        RoundSidesRectCanvasShape(boundsProducer)

    private val borderDrawer = BorderDrawer(
        context = context,
        borderInfo = info.border,
        canvasShape = canvasShape
    )

    init {
        background = null
        setPadding(info.paddingHorizontal, info.paddingVertical)

        typeface = info.font.get(context).typeface
        setTextColor(info.textColor.get(context))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, info.textSize.getPx(context))
        setText(text.get(context))
        gravity = info.gravity.resolveAndroidGravity()

        info.maxLength?.let { filters += InputFilter.LengthFilter(it) }
        setInputType(info.inputType)
        setSingleLine()
        transformationMethod = info.transformationMethod

        setHintTextColor(info.hintTextColor.get(context))
        setHint(hint.get(context))
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.doInState {
            translate(scrollX.toFloat(), scrollY.toFloat())
            borderDrawer.draw(canvas)
        }
    }

}

fun <G : ViewGroup> G.addSimpleInput(
    text: StringGetter = StringGetter.EMPTY,
    hint: StringGetter = StringGetter.EMPTY,
    info: SimpleInputViewInfo = SimpleInputViewInfo.DEFAULT,
    viewConfigurator: (SimpleInputView.() -> Unit)? = null
) =
    addChild(
        SimpleInputView(
            context,
            text,
            hint,
            info
        ),
        viewConfigurator
    )