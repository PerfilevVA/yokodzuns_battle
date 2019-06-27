package com.sibedge.yokodzun.android.layers.edit_test_task_variant_description_md

import android.content.ClipDescription
import android.content.Context
import android.widget.EditText
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyVerticalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.ifTrue
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.entity.TestTaskVariantWithId
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.input.multiline.MultilineInputView
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector


class EditTestTaskVariantDescriptionMDLayer(
    context: Context
) : AppLayer(
    context = context
) {
    companion object {

        fun newInstance(
            context: Context,
            variant: TestTaskVariantWithId,
            callback: EditTestTaskVariantDescriptionMDLayerCallback
        ) = EditTestTaskVariantDescriptionMDLayer(context).apply {
            this.variant = variant
            this.callback = callback
        }

    }

    @LayerState
    private lateinit var variant: TestTaskVariantWithId

    @LayerState
    private lateinit var callback: EditTestTaskVariantDescriptionMDLayerCallback

    override val title: StringGetter by lazy {
        StringGetter(R.string.edit_test_task_variant_description_md_layer_title, variant.numberUiString)
    }

    private val descriptionMDInputView: EditText by lazy {
        MultilineInputView(
            context = context,
            text = variant.value.descriptionMD.toGetter(),
            hint = title
        )
            .applyLinearParams {
                setStretchedHeight()
                setMatchParentWidth()
            }
    }

    override fun afterCreate() {
        super.afterCreate()

        content {

            applyVerticalOrientation()
            orientation = VERTICAL
            addChild(descriptionMDInputView)
            addBottomButtonView(
                text = StringGetter(R.string.dialog_save),
                onClick = {
                    val descriptionMD = descriptionMDInputView.text.toString()
                    updateDescriptionMD(descriptionMD)
                }
            )
        }
    }

    private fun updateDescriptionMD(descriptionMD: String) {
        callback.onVariantDescriptionMDChanged(variant.number, descriptionMD)
        managerConnector.goBack()
    }

    override fun handleGoBack(): Boolean {
        val newDescription = descriptionMDInputView.text.toString()
        (variant.value.descriptionMD == newDescription).ifTrue { return false }
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.edit_test_task_variant_description_md_layer_edited_but_not_saved_dialog_title),
            text = StringGetter(R.string.edit_test_task_variant_description_md_layer_edited_but_not_saved_dialog_text),
            confirmText = StringGetter(R.string.dialog_save)
        ) {
            updateDescriptionMD(newDescription)
        }
        return true
    }

}