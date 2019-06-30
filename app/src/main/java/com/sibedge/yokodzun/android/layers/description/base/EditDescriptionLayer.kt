package com.sibedge.yokodzun.android.layers.description.base

import android.content.Context
import android.text.InputType
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.view.description.DescriptionLogoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.view.input.multiline.MultilineInputView
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


abstract class EditDescriptionLayer(
    context: Context,
    private val titleHint: StringGetter,
    private val logoUrlHint: StringGetter,
    private val descriptionHint: StringGetter
) : AppLayer(
    context = context
) {

    protected abstract val initialDescription: Description

    override fun afterCreate() {
        super.afterCreate()

        content {

            addScrollView {

                isFillViewport = true
                applyPadding(SizeManager.LARGE_SEPARATION, SizeManager.DEFAULT_SEPARATION)
                applyLinearParams {
                    setMatchParentWidth()
                    setStretchedHeight()
                }

                addVerticalLayout {

                    addFgSmallInputLabelView(titleHint)

                    val titleInput =
                        SimpleInputView(
                            context = context,
                            text = initialDescription.title.toGetter()
                        )
                    addChild(titleInput)

                    val logoUrlInput =
                        SimpleInputView(
                            context = context,
                            text = initialDescription.logoUrl.toGetter(),
                            info = SimpleInputViewInfo(
                                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
                            )
                        )

                    val logoView = DescriptionLogoView(
                        context
                    ).apply {
                        applyLinearParams {
                            applyBottomGravity()
                            setStartMargin(SizeManager.SMALL_SEPARATION)
                            setEndMargin(SizeManager.SMALL_SEPARATION)
                        }
                        content = initialDescription
                    }

                    addHorizontalLayout {
                        addVerticalLayout {
                            applyLinearParams { setStretchedWidth() }
                            addFgSmallInputLabelView(logoUrlHint)
                            addChild(logoUrlInput)
                        }
                        addChild(logoView)
                    }

                    titleInput.setOnFocusChangeListener { _, focused ->
                        if (!focused) {
                            logoView.content =
                                logoView.content?.copy(title = titleInput.text.toString())
                        }
                    }

                    logoUrlInput.setOnFocusChangeListener { _, focused ->
                        if (!focused) {
                            logoView.content =
                                logoView.content?.copy(logoUrl = logoUrlInput.text.toString())
                        }
                    }


                    addFgSmallInputLabelView(descriptionHint)
                    val descriptionInput = MultilineInputView(
                        context = context,
                        text = initialDescription.description.toGetter(),
                        hint = descriptionHint + "..."
                    ).apply {
                        applyLinearParams {
                            setStretchedHeight()
                            setMatchParentWidth()
                        }
                    }
                    addChild(descriptionInput)

                    addBottomButtonView(
                        text = StringGetter(R.string.dialog_save),
                        onClick = {
                            val newDescription = Description(
                                title = titleInput.text.toString(),
                                logoUrl = logoUrlInput.text.toString(),
                                description = descriptionInput.text.toString()
                            )
                            save(newDescription)
                        }
                    )

                }

            }

        }

    }

    private fun save(
        editedDescription: Description
    ) = uiJobLocked {
        saveAsync(editedDescription)
        managerConnector.goBack()
    }

    protected abstract suspend fun saveAsync(editedDescription: Description)

}