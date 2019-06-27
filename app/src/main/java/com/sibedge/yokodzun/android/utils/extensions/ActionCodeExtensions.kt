package com.sibedge.yokodzun.android.utils.extensions

import android.widget.FrameLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.addView
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.action_code.ActionCodeView
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.android.utils.managers.closeButton
import ru.hnau.remote_teaching_common.data.ActionCodeType


private data class ActionCodeDescription(
    val title: StringGetter,
    val text: StringGetter
)

private val ACTION_CODE_TYPES_DESCRIPTIONS: Map<ActionCodeType, ActionCodeDescription> = hashMapOf(
    ActionCodeType.CREATE_TEACHER to ActionCodeDescription(
        title = StringGetter(R.string.action_code_type_description_title_create_teacher),
        text = StringGetter(R.string.action_code_type_description_text_create_teacher)
    ),
    ActionCodeType.CREATE_STUDENT_OF_GROUP to ActionCodeDescription(
        title = StringGetter(R.string.action_code_type_description_title_create_student_of_group),
        text = StringGetter(R.string.action_code_type_description_text_create_student_of_group)
    ),
    ActionCodeType.RESTORE_TEACHER_PASSWORD to ActionCodeDescription(
        title = StringGetter(R.string.action_code_type_description_title_restore_teacher_password),
        text = StringGetter(R.string.action_code_type_description_text_restore_teacher_password)
    ),
    ActionCodeType.RESTORE_STUDENT_PASSWORD to ActionCodeDescription(
        title = StringGetter(R.string.action_code_type_description_title_restore_student_password),
        text = StringGetter(R.string.action_code_type_description_text_restore_student_password)
    )
)

fun ActionCodeType.showInfoDialog(actionCode: String) =
    AppActivityConnector.showDialog {
        val (title, text) = ACTION_CODE_TYPES_DESCRIPTIONS.getValue(
            this@showInfoDialog
        )
        title(title)
        text(text)
        view(
            FrameLayout(context).apply {
                setTopPadding(SizeManager.SMALL_SEPARATION)
                addChild(
                    ActionCodeView(context, actionCode)
                        .applyPadding(
                            SizeManager.DEFAULT_SEPARATION,
                            SizeManager.SMALL_SEPARATION
                        )
                        .applyFrameParams {}
                )
            }

        )
        closeButton()
        goBackHandler = { true }
    }