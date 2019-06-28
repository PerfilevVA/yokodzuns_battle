package com.sibedge.yokodzun.android.layers

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.text.method.TransformationMethod
import android.widget.LinearLayout
import androidx.appcompat.text.AllCapsTransformationMethod
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.dp40
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.jutils.TimeValue
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addLargePrimaryBackgroundShadowButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addSmallFgUnderlineTextButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addSmallPrimaryTextAndBorderButtonView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.ui.input.simple.addSimpleInput
import com.sibedge.yokodzun.android.utils.managers.*
import com.sibedge.yokodzun.common.exception.ApiException
import com.sibedge.yokodzun.common.utils.RaterCodeUtils
import com.sibedge.yokodzun.common.utils.Validators
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter.Companion.dp
import ru.hnau.androidutils.context_getters.dp_px.dp16
import ru.hnau.androidutils.context_getters.dp_px.dp24
import ru.hnau.androidutils.context_getters.dp_px.dp32
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType
import ru.hnau.androidutils.ui.drawables.layout_drawable.view.addLayoutDrawableView
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import javax.xml.validation.TypeInfoProvider


class LoginLayer(
    context: Context
) : AppLayer(
    context = context,
    showGoBackButton = false
) {

    override val title = StringGetter(R.string.login_layer_title)

    private val loginLayerContentContainer =
        LinearLayout(context).apply {

            applyVerticalOrientation()
            applyCenterGravity()
            applyPadding(dp40, SizeManager.DEFAULT_SEPARATION)

            addLinearSeparator(4f)

            addHorizontalLayout {

                applyCenterGravity()

                addLayoutDrawableView(
                    content = DrawableGetter(R.drawable.ic_launcher),
                    layoutType = LayoutType.Inside
                ) {
                    applyLinearParams {
                        setSize(dp(96))
                    }
                }

                addLabel(
                    text = StringGetter(R.string.login_layer_logo_title),
                    gravity = HGravity.START_CENTER_VERTICAL,
                    textSize = dp24,
                    fontType = FontManager.UBUNTU_BOLD,
                    textColor = ColorManager.FG
                ) {
                    applyLinearParams {
                        setEndMargin(dp32)
                    }
                }
            }

            addLinearSeparator()

            val raterCodeInput = SimpleInputView(
                context = context,
                hint = StringGetter(R.string.login_layer_code_input_title),
                info = SimpleInputViewInfo.DEFAULT.copy(
                    textSize = SizeManager.TEXT_20,
                    gravity = HGravity.CENTER,
                    maxLength = RaterCodeUtils.LENGTH,
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                )
            ).apply {
                filters += InputFilter.AllCaps()
            }

            addView(raterCodeInput)

            addLinearSeparator(5f)

            KeyboardManager.showAndRequestFocus(raterCodeInput)

        }

    override fun afterCreate() {
        super.afterCreate()

        content {

            addChild(loginLayerContentContainer)

            if (SettingsManager.host.isBlank()) {
                postDelayed(TimeValue.SECOND) {
                    ErrorHandler.handle(ApiException.HOST_NOT_CONFIGURED)
                }
            }

        }

    }

}