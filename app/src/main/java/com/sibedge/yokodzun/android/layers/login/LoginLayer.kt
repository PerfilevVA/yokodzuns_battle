package com.sibedge.yokodzun.android.layers.login

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.layers.admin.AdminLayer
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.rater.RaterLayer
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.*
import com.sibedge.yokodzun.android.utils.tryOrHandleError
import com.sibedge.yokodzun.common.exception.ApiException
import com.sibedge.yokodzun.common.utils.CodeUtils
import com.sibedge.yokodzun.common.utils.Validators
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.dp40
import ru.hnau.androidutils.ui.drawables.layout_drawable.LayoutType
import ru.hnau.androidutils.ui.drawables.layout_drawable.view.addLayoutDrawableView
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.postDelayed
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.ifTrue


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

            applyCenterGravity()
            applyPadding(dp40, SizeManager.DEFAULT_SEPARATION)

            addLinearSeparator()

            addLayoutDrawableView(
                content = DrawableGetter(R.drawable.ic_logo_with_text),
                layoutType = LayoutType.Independent
            ) {
                applyLinearParams {
                    setBottomMargin(SizeManager.LARGE_SEPARATION)
                }
            }

            addLabel(
                fontType = FontManager.DEFAULT,
                textSize = SizeManager.TEXT_16,
                textColor = ColorManager.FG,
                text = StringGetter(R.string.login_layer_code_input_title),
                maxLines = 1,
                minLines = 1,
                gravity = HGravity.CENTER
            )

            val raterCodeInput = CodeInput(
                context = context,
                onEntered = this@LoginLayer::onEnteredCode
            )

            addView(raterCodeInput)

            addLinearSeparator()

            KeyboardManager.showAndRequestFocus(raterCodeInput)

        }

        if (SettingsManager.host.isBlank()) {
            postDelayed(TimeValue.SECOND) {
                ErrorHandler.handle(ApiException.HOST_NOT_CONFIGURED)
            }
        }
    }

    private fun onEnteredCode(code: String) {
        (code == CodeUtils.ADMIN_CODE).ifTrue {
            loginAsAdmin()
            return
        }
        loginAsRater(code)
    }

    private fun loginAsRater(raterCode: String) {
        uiJobLocked {
            AuthManager.loginAsRater(raterCode)
            AppActivityConnector.showLayer(::RaterLayer, true)
        }
    }

    private fun loginAsAdmin() = AppActivityConnector.showInputDialog(
        title = StringGetter(R.string.global_menu_login_as_admin_dialog_title),
        text = StringGetter(R.string.global_menu_login_as_admin_dialog_text),
        inputHint = StringGetter(R.string.global_menu_login_as_admin_dialog_hint),
        confirmButtonText = StringGetter(R.string.global_menu_login_as_admin_dialog_login_button),
        inputInfo = SimpleInputViewInfo(
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
            transformationMethod = PasswordTransformationMethod.getInstance()
        )
    ) { password ->
        tryOrHandleError {
            Validators.validatePasswordOrThrow(password)
            loginAsAdminWithPassword(password)
            true
        } ?: false
    }

    private fun loginAsAdminWithPassword(password: String) {
        uiJobLocked {
            AuthManager.loginAsAdmin(password)
            AppActivityConnector.showLayer(::AdminLayer, true)
        }
    }
}