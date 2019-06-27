package com.sibedge.yokodzun.android.utils.managers

import android.content.Context
import android.util.Range
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.manager.LayerManagerConnector
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.bottom_sheet.BottomSheetView
import ru.hnau.androidutils.ui.view.layer.preset.dialog.view.material.MaterialDialogView
import com.sibedge.yokodzun.android.AppActivity
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.input.simple.SimpleInputViewInfo
import com.sibedge.yokodzun.android.ui.plus_minus.PlusMinusColumnInfo


object AppActivityConnector {

    var appActivity: AppActivity? = null
        private set

    val layerManager: LayerManagerConnector?
        get() = appActivity?.layerManagerConnector

    fun onAppActivityCreated(appActivity: AppActivity) {
        AppActivityConnector.appActivity = appActivity
    }

    fun onAppActivityDestroyed(appActivity: AppActivity) = synchronized(this) {
        if (AppActivityConnector.appActivity == appActivity) {
            AppActivityConnector.appActivity = null
        }
    }

    fun showDialog(dialogViewConfigurator: MaterialDialogView.() -> Unit) {
        layerManager?.let {
            DialogManager.showDialog(
                it,
                dialogViewConfigurator
            )
        }
    }

    fun showConfirmDialog(
        title: StringGetter? = null,
        text: StringGetter? = null,
        confirmText: StringGetter = StringGetter(R.string.dialog_yes),
        onConfirm: () -> Unit
    ) {
        layerManager?.let {
            DialogManager.showConfirmDialog(
                it,
                title,
                text,
                confirmText,
                onConfirm
            )
        }
    }

    fun showInputDialog(
        title: StringGetter? = null,
        text: StringGetter? = null,
        inputInitialText: StringGetter = StringGetter.EMPTY,
        inputHint: StringGetter = StringGetter.EMPTY,
        inputInfo: SimpleInputViewInfo = SimpleInputViewInfo.DEFAULT,
        confirmButtonText: StringGetter = StringGetter(R.string.dialog_yes),
        onConfirm: (enteredText: String) -> Boolean
    ) {
        layerManager?.let {
            DialogManager.showInputDialog(
                it,
                title,
                text,
                inputInitialText,
                inputHint,
                inputInfo,
                confirmButtonText,
                onConfirm
            )
        }
    }

    fun <T : Comparable<T>> showPlusMinusDialog(
        title: StringGetter? = null,
        text: StringGetter? = null,
        initialValue: T,
        availableValueRange: ClosedRange<T>,
        columns: Collection<PlusMinusColumnInfo<T>>,
        valueToStringConverter: (T) -> StringGetter,
        confirmButtonText: StringGetter = StringGetter(R.string.dialog_yes),
        onConfirm: (enteredValue: T) -> Boolean
    ) {
        layerManager?.let {
            DialogManager.showPlusMinusDialog(
                it,
                title,
                text,
                initialValue,
                availableValueRange,
                columns,
                valueToStringConverter,
                confirmButtonText,
                onConfirm
            )
        }
    }

    fun showBottomSheet(
        bottomSheetViewConfigurator: BottomSheetView.() -> Unit
    ) {
        layerManager?.let {
            DialogManager.showBottomSheet(
                it,
                bottomSheetViewConfigurator
            )
        }
    }

    fun showLayer(
        layerBuilder: (Context) -> AppLayer,
        clearStack: Boolean = false
    ) {
        layerManager?.apply {
            showLayer(layerBuilder.invoke(viewContext), clearStack)
        }
    }

    fun goBack() =
        layerManager?.goBack() ?: false

}