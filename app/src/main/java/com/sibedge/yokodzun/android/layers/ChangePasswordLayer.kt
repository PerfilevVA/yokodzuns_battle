package com.sibedge.yokodzun.android.layers

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.utils.shortToast
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.exception.ApiException
import com.sibedge.yokodzun.common.utils.Validators


class ChangePasswordLayer(
    context: Context
) : AppLayer(
    context = context
) {

    override val title = StringGetter(R.string.change_password_layer_title)

    override fun afterCreate() {
        super.afterCreate()

        content {

            setPadding(SizeManager.LARGE_SEPARATION, SizeManager.DEFAULT_SEPARATION)

            addFgSmallInputLabelView(StringGetter(R.string.change_password_layer_old_password))

            val oldPasswordInput =
                SimpleInputView(
                    context = context,
                    info = SimpleInputViewInfo(
                        maxLength = Validators.MAX_PASSWORD_LENGTH,
                        transformationMethod = PasswordTransformationMethod.getInstance(),
                        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    )
                )
            addChild(oldPasswordInput)

            addFgSmallInputLabelView(StringGetter(R.string.change_password_layer_new_password))

            val passwordInput = SimpleInputView(
                context = context,
                info = SimpleInputViewInfo(
                    maxLength = Validators.MAX_PASSWORD_LENGTH,
                    transformationMethod = PasswordTransformationMethod.getInstance(),
                    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
            )
            addChild(passwordInput)


            addFgSmallInputLabelView(StringGetter(R.string.change_password_layer_new_password_repeat))

            val passwordRepeatInput =
                SimpleInputView(
                    context = context,
                    info = SimpleInputViewInfo(
                        maxLength = Validators.MAX_PASSWORD_LENGTH,
                        transformationMethod = PasswordTransformationMethod.getInstance(),
                        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    )
                )
            addChild(passwordRepeatInput)

            addLinearSeparator()

            addBottomButtonView(
                text = StringGetter(R.string.change_password_layer_change_password),
                onClick = {
                    changePassword(
                        oldPassword = oldPasswordInput.text.toString(),
                        newPassword = passwordInput.text.toString(),
                        newPasswordRepeat = passwordRepeatInput.text.toString()
                    )
                }
            )

        }

    }

    private fun changePassword(
        oldPassword: String,
        newPassword: String,
        newPasswordRepeat: String
    ) {
        uiJobLocked {

            Validators.validatePasswordOrThrow(oldPassword)
            Validators.validatePasswordOrThrow(newPassword)

            if (newPassword != newPasswordRepeat) {
                throw ApiException.raw(context.getString(R.string.change_password_layer_passwords_are_different))
            }

            API.adminChangePassword(
                oldPassword = oldPassword,
                newPassword = newPassword
            )

            shortToast(StringGetter(R.string.change_password_layer_success))
            managerConnector.goBack()
        }
    }

}