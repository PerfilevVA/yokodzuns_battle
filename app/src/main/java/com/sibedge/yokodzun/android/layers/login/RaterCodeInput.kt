package com.sibedge.yokodzun.android.layers.login

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.widget.EditText
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.gradient.YGradientPaint
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.utils.RaterCodeUtils
import ru.hnau.androidutils.ui.bounds_producer.BoundsProducer
import ru.hnau.androidutils.ui.bounds_producer.createBoundsProducer
import ru.hnau.androidutils.ui.canvas_shape.RoundSidesRectCanvasShape
import ru.hnau.androidutils.ui.drawer.Insets
import ru.hnau.androidutils.ui.drawer.shadow.drawer.ShadowDrawer
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.utils.types_utils.doInState
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.setPadding


class RaterCodeInput(
    context: Context,
    private val onEntered: (String) -> Unit
) : EditText(context) {

    private val shadowInsets = Insets(
        horizontal = SizeManager.DEFAULT_SEPARATION,
        vertical = SizeManager.SMALL_SEPARATION
    )

    private val boundsProducer =
        createBoundsProducer(false)
            .applyInsents(context, shadowInsets)

    private val canvasShape =
        RoundSidesRectCanvasShape(boundsProducer)

    private val shadowDrawer = ShadowDrawer(
        context = context,
        canvasShape = canvasShape,
        shadowInfo = ColorManager.DEFAULT_SHADOW_INFO
    )

    private val backgroundPaint = YGradientPaint(context)

    init {
        background = null
        applyPadding(
            shadowInsets.start + SizeManager.LARGE_SEPARATION,
            shadowInsets.top + SizeManager.SMALL_SEPARATION
        )
        gravity = Gravity.CENTER
        typeface = FontManager.BOLD.get(context).typeface
        setTextColor(ColorManager.FG.get(context))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeManager.TEXT_20.getPxInt(context).toFloat())
        filters += InputFilter.LengthFilter(RaterCodeUtils.LENGTH)
        filters += InputFilter.AllCaps()
        setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
        setSingleLine()
        boundsProducer.attach {
            backgroundPaint.setBounds(it.left, it.top, it.right, it.bottom)
        }
        addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable?) {
                val raterCode = editable?.toString()
                    ?.takeIf { it.length == RaterCodeUtils.LENGTH } ?: return
                onEntered(raterCode)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    override fun draw(canvas: Canvas) {
        canvas.doInState {
            translate(scrollX.toFloat(), scrollY.toFloat())
            shadowDrawer.draw(canvas)
            canvasShape.draw(canvas, backgroundPaint)
        }
        super.draw(canvas)
    }

}