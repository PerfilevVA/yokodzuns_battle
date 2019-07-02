package com.sibedge.yokodzun.android.layers.login

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.dp40
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.jutils.TimeValue
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.data.RaterBattleDataManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.rater.RaterLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.*
import com.sibedge.yokodzun.common.exception.ApiException
import com.sibedge.yokodzun.common.utils.RaterCodeUtils
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.dp_px.dp32
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType
import ru.hnau.androidutils.ui.drawables.layout_drawable.view.addLayoutDrawableView
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.base.get
import java.lang.Exception


class LoginLayer(
    context: Context
) : AppLayer(
    context = context,
    showGoBackButton = false
) {

    override val title = StringGetter(R.string.login_layer_title)

    override fun afterCreate() {
        super.afterCreate()

        content {

            addVerticalLayout {

                applyCenterGravity()
                applyPadding(dp40, SizeManager.DEFAULT_SEPARATION)

                addLinearSeparator(4f)

                addLayoutDrawableView(
                    content = DrawableGetter(R.drawable.ic_logo_with_text),
                    layoutType = LayoutType.Independent
                )

                addLinearSeparator()

                addLabel(
                    fontType = FontManager.DEFAULT,
                    textSize = SizeManager.TEXT_16,
                    textColor = ColorManager.FG,
                    text = StringGetter(R.string.login_layer_code_input_title),
                    maxLines = 1,
                    minLines = 1,
                    gravity = HGravity.CENTER
                )

                val raterCodeInput = RaterCodeInput(
                    context = context,
                    onEntered = this@LoginLayer::loginAsRater
                )

                addView(raterCodeInput)

                addLinearSeparator(5f)

                KeyboardManager.showAndRequestFocus(raterCodeInput)

            }

            if (SettingsManager.host.isBlank()) {
                postDelayed(TimeValue.SECOND) {
                    ErrorHandler.handle(ApiException.HOST_NOT_CONFIGURED)
                }
            }

        }

    }

    private fun loginAsRater(raterCode: String) {
        uiJobLocked {
            AuthManager.loginAsRater(raterCode)
            AppActivityConnector.showLayer(::RaterLayer, true)
        }
    }

}