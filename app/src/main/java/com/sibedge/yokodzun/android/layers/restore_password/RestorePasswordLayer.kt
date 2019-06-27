package com.sibedge.yokodzun.android.layers.restore_password

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
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.setPadding
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.main.MainLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallLabelView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.remote_teaching_common.utils.Validators
import ru.hnau.remote_teaching_common.data.UserRole
import ru.hnau.remote_teaching_common.exception.ApiException
import java.lang.IllegalArgumentException


class RestorePasswordLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            role: UserRole
        ) = RestorePasswordLayer(context).apply {
            this.restorePasswordContext = when (role) {
                UserRole.TEACHER -> RestorePasswordContext.Teacher
                UserRole.STUDENT -> RestorePasswordContext.Student
                else -> throw IllegalArgumentException("Unsupported for restore password role '${role.name}'")
            }
        }

    }

    override val title = StringGetter(R.string.restore_password_layer_title)

    @LayerState
    private lateinit var restorePasswordContext: RestorePasswordContext

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

                    addFgSmallInputLabelView(StringGetter(R.string.restore_password_layer_password_title))

                    val passwordInput = SimpleInputView(
                        context = context,
                        info = SimpleInputViewInfo(
                            maxLength = Validators.MAX_PASSWORD_LENGTH,
                            transformationMethod = PasswordTransformationMethod.getInstance(),
                            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                        )
                    )
                    addChild(passwordInput)


                    addFgSmallInputLabelView(StringGetter(R.string.restore_password_layer_password_repeat_title))

                    val passwordRepeatInput = SimpleInputView(
                        context = context,
                        info = SimpleInputViewInfo(
                            maxLength = Validators.MAX_PASSWORD_LENGTH,
                            transformationMethod = PasswordTransformationMethod.getInstance(),
                            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                        )
                    )
                    addChild(passwordRepeatInput)

                    addFgSmallInputLabelView(StringGetter(R.string.restore_password_layer_action_code_title))

                    val actionCodeInput = SimpleInputView(
                        context = context,
                        info = SimpleInputViewInfo(
                            maxLength = restorePasswordContext.actionCodeType.codeLength
                        )
                    ).apply {
                        filters += InputFilter.AllCaps()
                    }
                    addChild(actionCodeInput)

                    addFgSmallLabelView(restorePasswordContext.actionCodeComment)

                    addLinearSeparator()

                    addBottomButtonView(
                        text = StringGetter(R.string.restore_password_layer_login),
                        onClick = {
                            restorePassword(
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

    private fun restorePassword(
        password: String,
        passwordRepeat: String,
        actionCode: String
    ) {
        uiJobLocked {

            Validators.validateUserPasswordOrThrow(password)
            if (password != passwordRepeat) {
                throw ApiException.raw(context.getString(R.string.restore_password_layer_passwords_are_different))
            }

            Validators.validateActionCodeOrThrow(actionCode, restorePasswordContext.actionCodeType)

            val login = restorePasswordContext.restorePassword(actionCode, password)

            AuthManager.login(login, password)
            showLayer(MainLayer(context), true)
        }
    }

}