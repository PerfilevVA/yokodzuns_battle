package com.sibedge.yokodzun.android.layers

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.LinearLayout
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
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.main.MainLayer
import com.sibedge.yokodzun.android.layers.registration.RegistrationLayer
import com.sibedge.yokodzun.android.layers.restore_password.RestorePasswordLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addLargePrimaryBackgroundShadowButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addSmallFgUnderlineTextButtonView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.DialogManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.android.utils.extensions.title
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import com.sibedge.yokodzun.android.utils.managers.SettingsManager
import ru.hnau.remote_teaching_common.utils.Validators
import ru.hnau.remote_teaching_common.data.UserRole
import ru.hnau.remote_teaching_common.exception.ApiException


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

            addLinearSeparator()

            addFgSmallInputLabelView(StringGetter(R.string.login_layer_login))

            val loginInput = SimpleInputView(
                context = context,
                info = SimpleInputViewInfo(
                    maxLength = Validators.MAX_LOGIN_LENGTH
                ),
                text = (AuthManager.login ?: "").toGetter()
            )
            addChild(loginInput)

            addFgSmallInputLabelView(StringGetter(R.string.login_layer_password)) {
                applyTopPadding(SizeManager.EXTRA_SMALL_SEPARATION)
            }

            val passwordInput = SimpleInputView(
                context = context,
                info = SimpleInputViewInfo(
                    maxLength = Validators.MAX_PASSWORD_LENGTH,
                    transformationMethod = PasswordTransformationMethod.getInstance(),
                    inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
            )
            addChild(passwordInput)

            addLinearSeparator(
                weight = 0f,
                height = SizeManager.SMALL_SEPARATION
            )

            addLargePrimaryBackgroundShadowButtonView(
                text = StringGetter(R.string.login_layer_do_login),
                onClick = { login(loginInput.text.toString(), passwordInput.text.toString()) }
            )

            addLinearSeparator()

            addSmallFgUnderlineTextButtonView(
                text = StringGetter(R.string.login_layer_do_register),
                onClick = this@LoginLayer::register
            )

            addSmallFgUnderlineTextButtonView(
                text = StringGetter(R.string.login_layer_do_restore_password),
                onClick = this@LoginLayer::restore
            ).applyBottomPadding(SizeManager.DEFAULT_SEPARATION)
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

    private fun login(login: String, password: String) {
        uiJobLocked {
            Validators.validateUserLoginOrThrow(login)
            Validators.validateUserPasswordOrThrow(password)
            AuthManager.login(login, password)
            showLayer(MainLayer(context), true)
        }
    }

    private fun register() {
        uiJobLocked {
            val role = chooseStudentOrTeacher(
                title = StringGetter(R.string.login_layer_registration_title)
            )
            showLayer(RegistrationLayer.newInstance(context, role))
        }
    }

    private fun restore() {
        uiJobLocked {
            val role = chooseStudentOrTeacher(
                title = StringGetter(R.string.login_layer_restore_password_title)
            )
            showLayer(RestorePasswordLayer.newInstance(context, role))
        }
    }

    private suspend fun chooseStudentOrTeacher(
        title: StringGetter
    ): UserRole {
        val deferred = CompletableDeferred<UserRole>()

        DialogManager.showBottomSheet(managerConnector) {
            title(title)
            addOnClosedListener { deferred.completeExceptionally(CancellationException()) }
            listOf(UserRole.TEACHER, UserRole.STUDENT).forEach { role ->
                closeItem(role.title) { deferred.complete(role) }
            }
        }

        return deferred.await()
    }

}