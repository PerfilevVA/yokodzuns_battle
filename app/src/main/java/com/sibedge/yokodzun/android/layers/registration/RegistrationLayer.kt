package com.sibedge.yokodzun.android.layers.registration

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.addScrollView
import ru.hnau.androidutils.ui.view.utils.apply.addVerticalLayout
import ru.hnau.androidutils.ui.view.utils.apply.addView
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.setPadding
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.main.MainLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallLabelView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.ui.input.simple.addSimpleInput
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.remote_teaching_common.utils.Validators
import ru.hnau.remote_teaching_common.data.UserRole
import ru.hnau.remote_teaching_common.exception.ApiException
import java.lang.IllegalArgumentException


class RegistrationLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            role: UserRole
        ) = RegistrationLayer(context).apply {
            this.registrationContext = when (role) {
                UserRole.TEACHER -> RegistrationContext.Teacher
                UserRole.STUDENT -> RegistrationContext.Student
                else -> throw IllegalArgumentException("Unsupported for register role '${role.name}'")
            }
        }

    }

    override val title = StringGetter(R.string.registration_layer_title)

    @LayerState
    private lateinit var registrationContext: RegistrationContext

    override fun afterCreate() {
        super.afterCreate()

        content {

            addScrollView {

                isFillViewport = true
                applyLinearParams {
                    setMatchParentWidth()
                    setStretchedHeight()
                }

                addVerticalLayout {

                    setPadding(SizeManager.LARGE_SEPARATION, SizeManager.DEFAULT_SEPARATION)

                    addFgSmallInputLabelView(StringGetter(R.string.registration_layer_login_title))

                    val loginInput = SimpleInputView(
                        context = context,
                        info = SimpleInputViewInfo(
                            maxLength = Validators.MAX_LOGIN_LENGTH
                        )
                    )
                    addChild(loginInput)

                    addFgSmallInputLabelView(StringGetter(R.string.registration_layer_password_title))

                    val passwordInput = SimpleInputView(
                        context = context,
                        info = SimpleInputViewInfo(
                            maxLength = Validators.MAX_PASSWORD_LENGTH,
                            transformationMethod = PasswordTransformationMethod.getInstance(),
                            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                        )
                    )
                    addChild(passwordInput)


                    addFgSmallInputLabelView(StringGetter(R.string.registration_layer_password_repeat_title))

                    val passwordRepeatInput = SimpleInputView(
                        context = context,
                        info = SimpleInputViewInfo(
                            maxLength = Validators.MAX_PASSWORD_LENGTH,
                            transformationMethod = PasswordTransformationMethod.getInstance(),
                            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                        )
                    )
                    addChild(passwordRepeatInput)

                    addFgSmallInputLabelView(StringGetter(R.string.registration_layer_action_code_title))

                    val actionCodeInput = SimpleInputView(
                        context = context,
                        info = SimpleInputViewInfo(
                            maxLength = registrationContext.actionCodeType.codeLength
                        )
                    ).apply {
                        filters += InputFilter.AllCaps()
                    }
                    addChild(actionCodeInput)

                    addFgSmallLabelView(registrationContext.actionCodeComment)

                    addLinearSeparator()

                    addBottomButtonView(
                        text = StringGetter(R.string.registration_layer_registration),
                        onClick = {
                            register(
                                login = loginInput.text.toString(),
                                password = passwordInput.text.toString(),
                                passwordRepeat = passwordRepeatInput.text.toString(),
                                actionCode = actionCodeInput.text.toString()
                            )
                        }
                    )

                }

            }

        }

    }

    private fun register(
        login: String,
        password: String,
        passwordRepeat: String,
        actionCode: String
    ) {
        uiJobLocked {

            Validators.validateUserLoginOrThrow(login)
            Validators.validateUserPasswordOrThrow(password)
            if (password != passwordRepeat) {
                throw ApiException.raw(context.getString(R.string.registration_layer_passwords_are_different))
            }

            Validators.validateActionCodeOrThrow(actionCode, registrationContext.actionCodeType)

            registrationContext.register(actionCode, login, password)

            AuthManager.login(login, password)
            showLayer(MainLayer(context), true)
        }
    }

}