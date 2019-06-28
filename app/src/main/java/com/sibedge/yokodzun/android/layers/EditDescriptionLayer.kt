package com.sibedge.yokodzun.android.layers

import android.content.Context
import android.text.InputType
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.description.DescriptionLogoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addFgSmallInputLabelView
import com.sibedge.yokodzun.android.ui.input.multiline.MultilineInputView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.helpers.Description
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class EditDescriptionLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            title: StringGetter,
            editingDescription: Description,
            titleHint: StringGetter,
            logoUrlHint: StringGetter,
            descriptionHint: StringGetter,
            save: (
                    (suspend CoroutineScope.() -> Unit) -> Unit,
                    Description
            ) -> Unit
        ) = EditDescriptionLayer(context).apply {
            this.title = title
            this.initialDescription = editingDescription
            this.titleHint = titleHint
            this.logoUrlHint = logoUrlHint
            this.descriptionHint = descriptionHint
            this.save = save
        }

    }

    @LayerState
    override lateinit var title: StringGetter

    @LayerState
    private lateinit var initialDescription: Description

    @LayerState
    private lateinit var titleHint: StringGetter

    @LayerState
    private lateinit var logoUrlHint: StringGetter

    @LayerState
    private lateinit var descriptionHint: StringGetter

    @LayerState
    private lateinit var save: (
            (suspend CoroutineScope.() -> Unit) -> Unit,
            Description
    ) -> Unit

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

                    val titleInput = SimpleInputView(
                        context = context,
                        text = initialDescription.title.toGetter()
                    )
                    addChild(titleInput)

                    val logoUrlInput = SimpleInputView(
                        context = context,
                        text = initialDescription.logoUrl.toGetter(),
                        info = SimpleInputViewInfo(
                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
                        )
                    )

                    val logoView = DescriptionLogoView(context).apply {
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
                            save(this@EditDescriptionLayer::uiJobLocked, newDescription)
                        }
                    )

                }

            }

        }

    }

}