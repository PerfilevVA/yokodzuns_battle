package com.sibedge.yokodzun.android.layers

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.addView
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.setPadding
import ru.hnau.androidutils.utils.shortToast
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.data.MeInfoManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addSmallPrimaryTextAndBorderButtonView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.ui.input.simple.addSimpleInput
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import ru.hnau.remote_teaching_common.data.User
import ru.hnau.remote_teaching_common.utils.Validators


class ProfileLayer(
    context: Context
) : AppLayer(
    context = context
) {
    companion object {

        fun newInstance(
            context: Context,
            user: User
        ) = ProfileLayer(context).apply {
            this.user = user
        }

    }

    @LayerState
    private lateinit var user: User

    override val title = StringGetter(R.string.profile_layer_title)

    override fun afterCreate() {
        super.afterCreate()

        content {

            applyPadding(SizeManager.LARGE_SEPARATION, SizeManager.DEFAULT_SEPARATION)
            applyCenterGravity()

            addFgSmallInputLabelView(StringGetter(R.string.profile_layer_surname))

            val surnameInput = SimpleInputView(
                context = context,
                info = SimpleInputViewInfo(
                    maxLength = Validators.MAX_SURNAME_LENGTH
                ),
                text = user.surname.toGetter()
            )
            addChild(surnameInput)

            addFgSmallInputLabelView(StringGetter(R.string.profile_layer_name))

            val nameInput = SimpleInputView(
                context = context,
                info = SimpleInputViewInfo(
                    maxLength = Validators.MAX_NAME_LENGTH
                ),
                text = user.name.toGetter()
            )
            addChild(nameInput)

            addFgSmallInputLabelView(StringGetter(R.string.profile_layer_patronymic))

            val patronymicInput = SimpleInputView(
                context = context,
                info = SimpleInputViewInfo(
                    maxLength = Validators.MAX_PATRONYMIC_LENGTH
                ),
                text = user.patronymic.toGetter()
            )
            addChild(patronymicInput)

            addLinearSeparator()

            addSmallPrimaryTextAndBorderButtonView(
                text = StringGetter(R.string.profile_layer_change_password),
                onClick = { showLayer(ChangePasswordLayer(context)) }
            ) {
                applyLinearParams()
            }

            addLinearSeparator()

            addBottomButtonView(
                text = StringGetter(R.string.dialog_save),
                onClick = {
                    changeFIO(
                        user,
                        nameInput.text.toString(),
                        surnameInput.text.toString(),
                        patronymicInput.text.toString()
                    )
                }
            )

        }

    }

    private fun changeFIO(
        user: User,
        newName: String,
        newSurname: String,
        newPatronymic: String
    ) {
        uiJobLocked {

            Validators.validateUserNameOrThrow(newName)
            Validators.validateUserSurnameOrThrow(newSurname)
            Validators.validateUserPatronymicOrThrow(newPatronymic)

            if (
                user.name != newName ||
                user.surname != newSurname ||
                user.patronymic != newPatronymic
            ) {
                API.changeFIO(newName, newSurname, newPatronymic).await()
                MeInfoManager.updateValue(
                    user.copy(
                        name = newName,
                        surname = newSurname,
                        patronymic = newPatronymic
                    )
                )
            }

            shortToast(StringGetter(R.string.profile_layer_success))
            managerConnector.goBack()
        }
    }


}