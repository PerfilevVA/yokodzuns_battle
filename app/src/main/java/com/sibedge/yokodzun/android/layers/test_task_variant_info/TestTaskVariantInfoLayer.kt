package com.sibedge.yokodzun.android.layers.test_task_variant_info

import android.content.Context
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.utils.shortToast
import ru.hnau.androidutils.utils.showToast
import ru.hnau.jutils.getter.SuspendGetter
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.not
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.entity.TestTaskVariantOptionWithId
import com.sibedge.yokodzun.android.data.entity.TestTaskVariantWithId
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addMainActonButtonView
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.minusAt
import com.sibedge.yokodzun.android.utils.tryOrHandleError
import ru.hnau.remote_teaching_common.data.test.TestTaskVariant
import ru.hnau.remote_teaching_common.exception.ApiException
import ru.hnau.remote_teaching_common.utils.Validators


class TestTaskVariantInfoLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun create(
            context: Context,
            callback: TestTaskVariantInfoLayerCallback
        ) = edit(
            context,
            TestTaskVariantWithId(0, TestTaskVariant()),
            callback
        )

        fun edit(
            context: Context,
            variant: TestTaskVariantWithId,
            callback: TestTaskVariantInfoLayerCallback
        ) = TestTaskVariantInfoLayer(context).apply {
            this.initialVariant = variant
            this.callback = callback
            this.optionsProducer = ActualProducerSimple(variant.value.optionsMD)
        }

    }

    @LayerState
    private lateinit var initialVariant: TestTaskVariantWithId

    @LayerState
    private lateinit var callback: TestTaskVariantInfoLayerCallback

    @LayerState
    private lateinit var optionsProducer: ActualProducerSimple<List<String>>

    override val title: StringGetter
        get() = StringGetter(R.string.test_task_variant_info_layer_title, initialVariant.numberUiString)

    private val optionsWithIdAsyncProducer: Producer<GetterAsync<Unit, List<TestTaskVariantOptionWithId>>>
            by lazy {
                optionsProducer.map { variants ->
                    val optionWithId = variants.mapIndexed { i, option -> TestTaskVariantOptionWithId(i, option) }
                    SuspendGetter.simple(optionWithId)
                } as Producer<GetterAsync<Unit, List<TestTaskVariantOptionWithId>>>
            }

    override fun afterCreate() {
        super.afterCreate()

        content {

            val list = TestTaskVariantOptionsContainer(
                context = context,
                onClick = this@TestTaskVariantInfoLayer::onOptionClick,
                onDeleteClick = this@TestTaskVariantInfoLayer::onOptionDeleteClick,
                producer = optionsWithIdAsyncProducer,
                invalidator = {},
                onEmptyListInfoViewGenerator = {
                    EmptyInfoView(
                        context = context,
                        text = StringGetter(R.string.test_task_variant_info_layer_no_variants_title),
                        button = StringGetter(R.string.test_task_variant_info_layer_no_variants_button) to this@TestTaskVariantInfoLayer::onAddOptionClick
                    )
                }
            )

            addFrameLayout {

                applyLinearParams {
                    setStretchedHeight()
                    setMatchParentWidth()
                }

                addChild(list)

                addMainActonButtonView(
                    icon = DrawableGetter(R.drawable.ic_add_white),
                    title = StringGetter(R.string.test_task_variant_info_layer_add_variant),
                    needShowTitle = list.onListScrolledToTopProducer.not(),
                    onClick = this@TestTaskVariantInfoLayer::onAddOptionClick
                )

            }

        }

    }

    override fun handleGoBack(): Boolean {
        val isNewVariant = initialVariant.value.optionsMD.isEmpty()

        val newOptions = optionsProducer.currentState
        if (newOptions.isEmpty()) {
            isNewVariant.handle(
                onTrue = this::onEmptyOptionsForNewTaskVariant,
                onFalse = this::onEmptyOptionsForEditingTaskVariant
            )
            return true
        }

        if (newOptions == initialVariant.value.optionsMD) {
            return false
        }

        val taskVariantWithOptions = initialVariant.value.copy(optionsMD = newOptions)
        if (isNewVariant) {
            callback.onVariantCreated(taskVariantWithOptions)
        } else {
            callback.onVariantEdited(initialVariant.number, taskVariantWithOptions)
        }

        return false
    }

    private fun onEmptyOptionsForNewTaskVariant() {
        AppActivityConnector.showDialog {
            title(StringGetter(R.string.test_task_variant_info_layer_new_task_empty_variants_dialog_title))
            text(StringGetter(R.string.test_task_variant_info_layer_new_task_empty_variants_dialog_text))
            closeButton(StringGetter(R.string.dialog_cancel))
            closeButton(StringGetter(R.string.test_task_variant_info_layer_new_task_empty_variants_dialog_remove_button)) {
                managerConnector.goBack()
            }
        }
    }

    private fun onEmptyOptionsForEditingTaskVariant() {
        AppActivityConnector.showDialog {
            title(StringGetter(R.string.test_task_variant_info_layer_editing_task_empty_variants_dialog_title))
            text(StringGetter(R.string.test_task_variant_info_layer_editing_task_empty_variants_dialog_text))
            closeButton(StringGetter(R.string.dialog_cancel))
            closeButton(StringGetter(R.string.test_task_variant_info_layer_editing_task_empty_variants_dialog_cancel_button)) {
                managerConnector.goBack()
            }
        }
    }

    private fun onOptionClick(option: TestTaskVariantOptionWithId) {
        onOptionDeleteClick(option)
    }

    private fun onOptionDeleteClick(option: TestTaskVariantOptionWithId) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.test_task_variant_info_layer_option_delete_option_confirm_dialog_title),
            text = StringGetter(
                R.string.test_task_variant_info_layer_option_delete_option_confirm_dialog_text,
                option.value
            ),
            confirmText = StringGetter(R.string.dialog_delete)
        ) {
            val newOptions = optionsProducer.currentState.minusAt(option.number)
            optionsProducer.updateState(newOptions)
            showToast(
                StringGetter(
                    R.string.test_task_variant_info_layer_option_delete_option_success,
                    option.value
                )
            )
        }
    }

    private fun onAddOptionClick() {
        AppActivityConnector.showInputDialog(
            title = StringGetter(R.string.test_task_variant_info_layer_create_new_test_dialog_title),
            text = StringGetter(R.string.test_task_variant_info_layer_create_new_test_dialog_text),
            confirmButtonText = StringGetter(R.string.test_task_variant_info_layer_create_new_test_dialog_button),
            inputInfo = SimpleInputViewInfo(
                maxLength = Validators.MAX_TEST_TASK_VARIANT_OPTION_MD_LENGTH
            ),
            onConfirm = this::onNewOptionEntered
        )
    }

    private fun onNewOptionEntered(optionMD: String): Boolean {
        tryOrHandleError {
            Validators.validateTestTaskVariantOptionMDOrThrow(optionMD)
            optionsProducer.currentState
                .find { it.equals(optionMD, true) }?.let {
                    val error = context.getString(
                        R.string.test_task_variant_info_layer_create_new_test_dialog_not_unique,
                        optionMD
                    )
                    throw ApiException.raw(error)
                }
            Unit
        } ?: return false

        optionsProducer.updateState(optionsProducer.currentState + optionMD)
        shortToast(StringGetter(R.string.test_task_variant_info_layer_create_new_test_success, optionMD))

        return true
    }


}