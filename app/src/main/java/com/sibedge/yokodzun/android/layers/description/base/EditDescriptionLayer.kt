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
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.ifTrue


abstract class EditDescriptionLayer(
    context: Context,
    private val titleHint: StringGetter,
    private val logoUrlHint: StringGetter,
    private val descriptionHint: StringGetter
) : AppLayer(
    context = context
) {

    protected abstract val initialDescription: Description

    private val titleInput by lazy {
        SimpleInputView(
            context = context,
            text = initialDescription.title.toGetter()
        )
    }

    private val logoUrlInput by lazy {
        SimpleInputView(
            context = context,
            text = initialDescription.logoUrl.toGetter(),
            info = SimpleInputViewInfo(
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
            )
        )
    }

    private val descriptionInput by lazy {
        MultilineInputView(
            context = context,
            text = initialDescription.description.toGetter(),
            hint = descriptionHint + "..."
        ).apply {
            applyLinearParams {
                setStretchedHeight()
                setMatchParentWidth()
            }
        }
    }

    private val editedDescription
        get() = Description(
            title = titleInput.text.toString(),
            logoUrl = logoUrlInput.text.toString(),
            description = descriptionInput.text.toString()
        )


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

                    applyPadding(SizeManager.LARGE_SEPARATION, SizeManager.DEFAULT_SEPARATION)

                    addFgSmallInputLabelView(titleHint)
                    addChild(titleInput)

                    val logoView = DescriptionLogoView(context)
                        .apply {
                            applyLinearParams {
                                applyBottomGravity()
                                setStartMargin(SizeManager.SMALL_SEPARATION)
                                setEndMargin(SizeManager.SMALL_SEPARATION)
                            }
                            data = initialDescription
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
                            logoView.data =
                                logoView.data?.copy(title = titleInput.text.toString())
                        }
                    }

                    logoUrlInput.setOnFocusChangeListener { _, focused ->
                        if (!focused) {
                            logoView.data =
                                logoView.data?.copy(logoUrl = logoUrlInput.text.toString())
                        }
                    }


                    addFgSmallInputLabelView(descriptionHint)

                    addChild(descriptionInput)

                    addBottomButtonView(
                        text = StringGetter(R.string.dialog_save),
                        onClick = { save(editedDescription) }
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

    override fun handleGoBack(): Boolean {
        val editedDescription = this.editedDescription
        (editedDescription == initialDescription).ifTrue { return false }
        AppActivityConnector.showDialog {
            title(StringGetter(R.string.edit_description_layer_warning_not_saved_title))
            text(StringGetter(R.string.edit_description_layer_warning_not_saved_text))
            closeButton(StringGetter(R.string.edit_description_layer_warning_not_saved_reset_button)) { managerConnector.goBack() }
            closeButton(StringGetter(R.string.dialog_save)) { save(editedDescription) }
        }
        return true
    }

}