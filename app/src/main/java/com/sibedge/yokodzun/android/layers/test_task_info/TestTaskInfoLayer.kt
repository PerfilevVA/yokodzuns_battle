package com.sibedge.yokodzun.android.layers.test_task_info

import android.content.Context
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.utils.showToast
import ru.hnau.jutils.getter.SuspendGetter
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.handle
import ru.hnau.jutils.ifTrue
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.not
import ru.hnau.jutils.toSingleItemList
import ru.hnau.jutils.tryOrElse
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.entity.TestTaskVariantWithId
import com.sibedge.yokodzun.android.data.entity.TestTaskWithId
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.edit_test_task_variant_description_md.EditTestTaskVariantDescriptionMDLayer
import com.sibedge.yokodzun.android.layers.edit_test_task_variant_description_md.EditTestTaskVariantDescriptionMDLayerCallback
import com.sibedge.yokodzun.android.layers.test_task_variant_info.TestTaskVariantInfoLayer
import com.sibedge.yokodzun.android.layers.test_task_variant_info.TestTaskVariantInfoLayerCallback
import com.sibedge.yokodzun.android.ui.RTCheckBox
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addMainActonButtonView
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ErrorHandler
import com.sibedge.yokodzun.android.utils.minusAt
import ru.hnau.remote_teaching_common.data.test.TestTask
import ru.hnau.remote_teaching_common.data.test.TestTaskType
import ru.hnau.remote_teaching_common.data.test.TestTaskVariant
import ru.hnau.remote_teaching_common.utils.Validators


class TestTaskInfoLayer(
    context: Context
) : AppLayer(
    context = context
), TestTaskVariantInfoLayerCallback, EditTestTaskVariantDescriptionMDLayerCallback {

    companion object {

        fun create(
            context: Context,
            taskType: TestTaskType,
            callback: TestTaskInfoLayerCallback
        ) = edit(
            context,
            TestTaskWithId(0, TestTask(type = taskType)),
            callback
        )

        fun edit(
            context: Context,
            testTask: TestTaskWithId,
            callback: TestTaskInfoLayerCallback
        ) = TestTaskInfoLayer(context).apply {
            this.initialTask = testTask
            this.callback = callback
            this.variantsProducer = ActualProducerSimple(testTask.value.variants)
        }

    }

    @LayerState
    private lateinit var initialTask: TestTaskWithId

    @LayerState
    private lateinit var callback: TestTaskInfoLayerCallback

    @LayerState
    private lateinit var variantsProducer: ActualProducerSimple<List<TestTaskVariant>>

    override val title: StringGetter
        get() = StringGetter(R.string.test_task_info_layer_title, initialTask.numberUiString)

    private val isNewTask: Boolean
            by lazy { initialTask.value.variants.isEmpty() }

    private val variantsWithIdAsyncProducer: Producer<GetterAsync<Unit, List<TestTaskVariantWithId>>>
            by lazy {
                variantsProducer.map { variants ->
                    val variantsWithId = variants.mapIndexed { i, variant -> TestTaskVariantWithId(i, variant) }
                    SuspendGetter.simple(variantsWithId)
                } as Producer<GetterAsync<Unit, List<TestTaskVariantWithId>>>
            }

    override fun afterCreate() {
        super.afterCreate()

        content {

            val list = TestTaskVariantsContainer(
                context = context,
                taskType = initialTask.value.type,
                onClick = this@TestTaskInfoLayer::onVariantClick,
                onMenuClick = this@TestTaskInfoLayer::onVariantMenuClick,
                producer = variantsWithIdAsyncProducer,
                invalidator = {},
                onEmptyListInfoViewGenerator = {
                    EmptyInfoView(
                        context = context,
                        text = StringGetter(R.string.test_task_info_layer_no_variants_title),
                        button = StringGetter(R.string.test_task_info_layer_no_variants_button) to this@TestTaskInfoLayer::onAddVariantClick
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
                    title = StringGetter(R.string.test_task_info_layer_add_variant),
                    needShowTitle = list.onListScrolledToTopProducer.not(),
                    onClick = this@TestTaskInfoLayer::onAddVariantClick
                )

            }

        }

    }

    override fun handleGoBack(): Boolean {

        val newVariants = variantsProducer.currentState
        if (newVariants.isEmpty()) {
            showIncorrectTaskInfoDialog(
                title = StringGetter(R.string.test_task_info_layer_no_variants_dialog_title),
                text = StringGetter(R.string.test_task_info_layer_no_variants_dialog_text)
            )
            return true
        }

        if (newVariants == initialTask.value.variants) {
            return false
        }

        if (!checkVariants(newVariants, initialTask.value.type)) {
            return true
        }

        val taskWithvariants = initialTask.value.copy(variants = newVariants)
        if (isNewTask) {
            callback.onTaskCreated(taskWithvariants)
        } else {
            callback.onTaskEdited(initialTask.number, taskWithvariants)
        }

        return false
    }

    private fun checkVariants(
        variants: List<TestTaskVariant>,
        taskType: TestTaskType
    ): Boolean {
        variants.forEachIndexed { number, variant ->
            if (!validateVariant(number, taskType, variant)) {
                return false
            }
        }
        return true
    }

    private fun validateVariant(
        number: Int,
        taskType: TestTaskType,
        variant: TestTaskVariant
    ): Boolean {
        if (variant.descriptionMD.isEmpty()) {
            showIncorrectTaskInfoDialog(
                title = StringGetter(
                    R.string.test_task_info_layer_no_description_dialog_title,
                    (number + 1).toString()
                ),
                text = StringGetter(R.string.test_task_info_layer_no_description_dialog_text)
            )
            return false
        }
        if (variant.responseParts.isEmpty() && taskType != TestTaskType.MULTI) {
            showIncorrectTaskInfoDialog(
                title = StringGetter(
                    R.string.test_task_info_layer_no_response_dialog_title,
                    (number + 1).toString()
                ),
                text = StringGetter(R.string.test_task_info_layer_no_response_dialog_text)
            )
            return false
        }
        return true
    }

    private fun showIncorrectTaskInfoDialog(
        title: StringGetter,
        text: StringGetter
    ) {
        AppActivityConnector.showDialog {
            title(title)
            text(text)
            closeButton(StringGetter(R.string.dialog_cancel))
            val dischardButtonTitle = isNewTask.handle(
                forTrue = StringGetter(R.string.test_task_info_layer_remove_task_button),
                forFalse = StringGetter(R.string.test_task_info_layer_dischard_task_button)
            )
            closeButton(dischardButtonTitle) {
                managerConnector.goBack()
            }
        }
    }

    private fun onVariantClick(variant: TestTaskVariantWithId) {
        if (initialTask.value.type == TestTaskType.TEXT) {
            onVariantMenuClick(variant)
            return
        }
        showLayer(TestTaskVariantInfoLayer.edit(context, variant, this))
    }

    private fun onVariantMenuClick(variant: TestTaskVariantWithId) {
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.test_task_info_layer_variant_options_title, variant.numberUiString))
            closeItem(StringGetter(R.string.test_task_info_layer_variant_options_description)) {
                onChangeVariantDescriptionClick(variant)
            }
            closeItem(StringGetter(R.string.test_task_info_layer_variant_options_response)) {
                onChangeVariantResponseClick(variant)
            }
            closeItem(StringGetter(R.string.dialog_delete)) {
                onDeleteVariantClick(variant)
            }
        }
    }

    private fun onChangeVariantDescriptionClick(variant: TestTaskVariantWithId) {
        showLayer(
            EditTestTaskVariantDescriptionMDLayer.newInstance(
                context = context,
                variant = variant,
                callback = this
            )
        )
    }

    override fun onVariantDescriptionMDChanged(number: Int, descriptionMD: String) {
        updateVariants { variants ->
            variants.mapIndexed { index, variant ->
                (index == number).handle(variant.copy(descriptionMD = descriptionMD), variant)
            }
        }
    }

    private fun onChangeVariantResponseClick(variant: TestTaskVariantWithId) {
        when (initialTask.value.type) {
            TestTaskType.SINGLE -> onChangeVariantResponseOfSingleResponseTaskClick(variant)
            TestTaskType.MULTI -> onChangeVariantResponseOfMultiResponseTaskClick(variant)
            TestTaskType.TEXT -> onChangeVariantResponseOfTextResponseTaskClick(variant)
        }
    }

    private fun onChangeVariantResponseOfSingleResponseTaskClick(variant: TestTaskVariantWithId) {
        showAddVariantOptionDialogIfNoOptions(variant).ifTrue { return }
        AppActivityConnector.showBottomSheet {
            title(
                StringGetter(
                    R.string.test_task_info_layer_choose_response_for_single_response_task_title,
                    variant.numberUiString
                )
            )
            variant.value.optionsMD.forEachIndexed { responseNumber, response ->
                closeItem(response.toGetter()) {
                    val responseParts = responseNumber.toString().toSingleItemList()
                    updateVariant(variant.number, variant.value.copy(responseParts = responseParts))
                }
            }
        }
    }

    private fun onChangeVariantResponseOfMultiResponseTaskClick(variant: TestTaskVariantWithId) {
        showAddVariantOptionDialogIfNoOptions(variant).ifTrue { return }
        AppActivityConnector.showBottomSheet {
            title(StringGetter(R.string.test_task_info_layer_variant_response_multi_title, variant.numberUiString))
            text(StringGetter(R.string.test_task_info_layer_variant_response_multi_text))

            val checkedOptions = variant.value.responseParts.map(String::toInt).toSet()
            val checkBoxes = variant.value.optionsMD.mapIndexed { i, optionMD ->
                RTCheckBox(
                    context = context,
                    index = i,
                    checked = i in checkedOptions,
                    text = optionMD
                )
            }

            checkBoxes.forEach(this::view)

            closeItem(StringGetter(R.string.dialog_save)) {
                val selectedOptionsMD = checkBoxes
                    .filter { it.checked }
                    .map { it.index.toString() }
                updateVariant(variant.number, variant.value.copy(responseParts = selectedOptionsMD))
            }
        }
    }

    private fun showAddVariantOptionDialogIfNoOptions(variant: TestTaskVariantWithId): Boolean {
        variant.value.optionsMD.isNotEmpty().ifTrue { return false }
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.test_task_info_layer_add_option_dialog_title),
            text = StringGetter(R.string.test_task_info_layer_add_option_dialog_text),
            confirmText = StringGetter(R.string.test_task_info_layer_add_option_dialog_button)
        ) {
            showLayer(TestTaskVariantInfoLayer.create(context, this))
        }
        return true
    }

    private fun onChangeVariantResponseOfTextResponseTaskClick(variant: TestTaskVariantWithId) {
        AppActivityConnector.showInputDialog(
            title = StringGetter(R.string.test_task_info_layer_edit_text_response_dialog_title),
            text = StringGetter(R.string.test_task_info_layer_edit_text_response_dialog_text),
            inputInitialText = variant.value.responseParts.firstOrNull()?.toGetter() ?: StringGetter.EMPTY,
            confirmButtonText = StringGetter(R.string.dialog_save),
            onConfirm = { response ->
                onResponseOfTextResponseTaskEntered(variant, response)
            }
        )
    }

    private fun onResponseOfTextResponseTaskEntered(
        variant: TestTaskVariantWithId,
        response: String
    ): Boolean {
        val responseParts = listOf(response)
        tryOrElse(
            throwsAction = {
                Validators.validateTestTaskVariantResponseOrThrow(
                    responseParts = responseParts,
                    optionsMD = variant.value.optionsMD,
                    taskType = TestTaskType.TEXT
                )
            },
            onThrow = {
                ErrorHandler.handle(it)
                return false
            }
        )

        updateVariant(
            number = variant.number,
            editedVariant = variant.value.copy(responseParts = responseParts)
        )

        showToast(
            StringGetter(
                R.string.test_task_info_layer_edit_text_response_success,
                response
            )
        )
        return true
    }

    private fun onDeleteVariantClick(variant: TestTaskVariantWithId) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.test_task_info_layer_option_delete_option_confirm_dialog_title),
            text = StringGetter(
                R.string.test_task_info_layer_option_delete_option_confirm_dialog_text,
                variant.numberUiString
            ),
            confirmText = StringGetter(R.string.dialog_delete)
        ) {
            updateVariants { it.minusAt(variant.number) }
            showToast(
                StringGetter(
                    R.string.test_task_info_layer_option_delete_option_success,
                    variant.numberUiString
                )
            )
        }
    }

    private fun onAddVariantClick() {
        if (initialTask.value.type == TestTaskType.TEXT) {
            this.updateVariants { variants -> variants + TestTaskVariant() }
            return
        }
        showLayer(TestTaskVariantInfoLayer.create(context, this))
    }

    private fun updateVariants(editor: (List<TestTaskVariant>) -> List<TestTaskVariant>) =
        synchronized(this) {
            val newVariants = editor(variantsProducer.currentState)
            variantsProducer.updateState(newVariants)
        }

    override fun onVariantEdited(number: Int, editedVariant: TestTaskVariant) {
        updateVariant(number, editedVariant.copy(responseParts = emptyList()))
    }

    private fun updateVariant(number: Int, editedVariant: TestTaskVariant) {
        this.updateVariants { variants ->
            variants.mapIndexed { index, variant ->
                (index == number).handle(editedVariant, variant)
            }
        }
    }

    override fun onVariantCreated(variant: TestTaskVariant) =
        this.updateVariants { variants -> variants + variant }

}