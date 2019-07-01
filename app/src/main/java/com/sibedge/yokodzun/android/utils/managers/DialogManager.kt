package com.sibedge.yokodzun.android.utils.managers

import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.utils.Side
import ru.hnau.androidutils.ui.view.layer.manager.LayerManagerConnector
import ru.hnau.androidutils.ui.view.layer.preset.dialog.DialogLayer
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.bottom_sheet.BottomSheetView
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.bottom_sheet.BottomSheetViewInfo
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.bottom_sheet.item.BottomSheetItemInfo
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.bottom_sheet.text.BottomSheetTextInfo
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.bottom_sheet.title.BottomSheetTitleInfo
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.material.MaterialDialogView
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.material.MaterialDialogViewInfo
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.material.button.MaterialDialogButtonInfo
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.material.text.MaterialDialogTextInfo
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.material.title.MaterialDialogTitleInfo
import ru.hnau.androidutils.ui.view.layer.transaction.TransactionInfo
import ru.hnau.androidutils.ui.view.utils.*
import ru.hnau.jutils.TimeValue
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputView
import com.sibedge.yokodzun.android.ui.view.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.ui.view.plus_minus.PlusMinusColumnInfo
import com.sibedge.yokodzun.android.ui.view.plus_minus.PlusMinusView
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.bottom_sheet.decoration.BottomSheetViewDecorationInfo
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.material.decoration.MaterialDialogViewDecorationInfo


object DialogManager {

    fun showDialog(
        layerManager: LayerManagerConnector,
        dialogViewConfigurator: MaterialDialogView.() -> Unit
    ) {
        val dialogBuilder = MaterialDialogView.create(
            info = MATERIAL_DIALOG_VIEW_INFO,
            dialogViewConfigurator = dialogViewConfigurator
        )
        layerManager.showLayer(
            layer = DialogLayer.create(
                context = layerManager.viewContext,
                dialogViewBuilder = dialogBuilder
            ),
            transactionInfo = TransactionInfo(
                emersionSide = Side.BOTTOM
            )
        )
    }

    fun showConfirmDialog(
        layerManager: LayerManagerConnector,
        title: StringGetter? = null,
        text: StringGetter? = null,
        confirmButtonText: StringGetter = StringGetter(R.string.dialog_yes),
        onConfirm: () -> Unit
    ) = showDialog(layerManager) {
        title?.let(this::title)
        text?.let(this::text)
        cancelButton()
        closeButton(confirmButtonText, onClick = onConfirm)
    }

    fun showInputDialog(
        layerManager: LayerManagerConnector,
        title: StringGetter? = null,
        text: StringGetter? = null,
        inputInitialText: StringGetter = StringGetter.EMPTY,
        inputHint: StringGetter = StringGetter.EMPTY,
        inputInfo: SimpleInputViewInfo = SimpleInputViewInfo.DEFAULT,
        confirmButtonText: StringGetter = StringGetter(R.string.dialog_yes),
        inputConfigurator: SimpleInputView.() -> Unit = {},
        onConfirm: (enteredText: String) -> Boolean
    ) = showDialog(layerManager) {

        val inputView = SimpleInputView(
            context = context,
            text = inputInitialText,
            info = inputInfo,
            hint = inputHint
        ).apply {
            setLinearParams(MATCH_PARENT, WRAP_CONTENT) {
                setTopMargin(SizeManager.DEFAULT_SEPARATION.getPxInt(context))
            }
            inputConfigurator()
        }

        title?.let(this::title)
        text?.let(this::text)
        view(inputView)
        cancelButton()

        button(confirmButtonText) {
            val enteredText = inputView.text.toString()
            val close = onConfirm.invoke(enteredText)
            if (close) {
                close()
            }
        }

        addOnClosedListener {
            KeyboardManager.hide()
        }

        postDelayed(TimeValue.MILLISECOND * 100) {
            KeyboardManager.showAndRequestFocus(inputView)
        }
    }

    fun <T : Comparable<T>> showPlusMinusDialog(
        layerManager: LayerManagerConnector,
        title: StringGetter? = null,
        text: StringGetter? = null,
        initialValue: T,
        availableValueRange: ClosedRange<T>,
        columns: Collection<PlusMinusColumnInfo<T>>,
        valueToStringConverter: (T) -> StringGetter,
        confirmButtonText: StringGetter = StringGetter(R.string.dialog_yes),
        onConfirm: (enteredValue: T) -> Boolean
    ) = showDialog(layerManager) {

        val plusMinusView =
            PlusMinusView(
                context = context,
                initialValue = initialValue,
                availableValueRange = availableValueRange,
                columns = columns,
                valueToStringConverter = valueToStringConverter
            ).apply {
                setLinearParams(MATCH_PARENT, WRAP_CONTENT) {
                    setTopMargin(SizeManager.DEFAULT_SEPARATION.getPxInt(context))
                }
            }

        var value: T? = null
        plusMinusView.valueProducer.attach { value = it }

        title?.let(this::title)
        text?.let(this::text)
        view(plusMinusView)
        cancelButton()

        button(confirmButtonText) {
            value?.let {
                val close = onConfirm.invoke(it)
                if (close) {
                    close()
                }
            }
        }
    }

    fun showBottomSheet(
        layerManager: LayerManagerConnector,
        bottomSheetViewConfigurator: BottomSheetView.() -> Unit
    ) {
        val dialogBuilder = BottomSheetView.create(
            info = BOTTOM_SHEET_VIEW_INFO,
            dialogViewConfigurator = bottomSheetViewConfigurator
        )
        layerManager.showLayer(
            layer = DialogLayer.create(
                context = layerManager.viewContext,
                dialogViewBuilder = dialogBuilder
            ),
            transactionInfo = TransactionInfo(
                emersionSide = Side.BOTTOM
            )
        )
    }

    private val MATERIAL_DIALOG_VIEW_INFO = MaterialDialogViewInfo(
        button = MaterialDialogButtonInfo.DEFAULT.copy(
            textFont = FontManager.BOLD,
            textColor = ColorManager.PRIMARY,
            rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO,
            textSize = SizeManager.TEXT_12
        ),
        title = MaterialDialogTitleInfo.DEFAULT.copy(
            labelInfo = MaterialDialogTitleInfo.DEFAULT.labelInfo.copy(
                textColor = ColorManager.PRIMARY,
                fontType = FontManager.BOLD,
                textSize = SizeManager.TEXT_20
            )
        ),
        text = MaterialDialogTextInfo.DEFAULT.copy(
            labelInfo = MaterialDialogTextInfo.DEFAULT.labelInfo.copy(
                textColor = ColorManager.FG,
                fontType = FontManager.DEFAULT,
                textSize = SizeManager.TEXT_16
            )
        ),
        decoration = MaterialDialogViewDecorationInfo(
            background = ColorManager.BG_LIGHT,
            shadow = ColorManager.DEFAULT_SHADOW_INFO
        )
    )

    private val BOTTOM_SHEET_VIEW_INFO = BottomSheetViewInfo(
        title = BottomSheetTitleInfo.DEFAULT.copy(
            labelInfo = BottomSheetTitleInfo.DEFAULT.labelInfo.copy(
                textColor = ColorManager.PRIMARY,
                fontType = FontManager.BOLD,
                textSize = SizeManager.TEXT_16
            )
        ),
        text = BottomSheetTextInfo.DEFAULT.copy(
            labelInfo = BottomSheetTextInfo.DEFAULT.labelInfo.copy(
                textColor = ColorManager.FG,
                fontType = FontManager.DEFAULT,
                textSize = SizeManager.TEXT_12
            )
        ),
        item = BottomSheetItemInfo.DEFAULT.copy(
            labelInfo = BottomSheetItemInfo.DEFAULT.labelInfo.copy(
                textColor = ColorManager.FG,
                fontType = FontManager.DEFAULT,
                textSize = SizeManager.TEXT_16
            ),
            rippleDrawInfo = ColorManager.FG_ON_TRANSPARENT_RIPPLE_INFO
        ),
        decoration = BottomSheetViewDecorationInfo(
            backgroundColor = ColorManager.BG_LIGHT
        )
    )

}

fun MaterialDialogView.closeButton(onClick: () -> Unit = {}) =
    closeButton(StringGetter(R.string.dialog_close), onClick = onClick)

fun MaterialDialogView.cancelButton(onClick: () -> Unit = {}) =
    closeButton(StringGetter(R.string.dialog_cancel), onClick = onClick)

fun MaterialDialogView.yesButton(onClick: () -> Unit) =
    closeButton(StringGetter(R.string.dialog_yes), onClick = onClick)

fun MaterialDialogView.noButton(onClick: () -> Unit = {}) =
    closeButton(StringGetter(R.string.dialog_no), onClick = onClick)