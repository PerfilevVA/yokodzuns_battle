package com.sibedge.yokodzun.android.layers.battle.yokodzuns.edit

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.YokodzunsDataManager
import com.sibedge.yokodzun.android.layers.select.SelectYokodzunForBattleLayer
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.jutils.getter.base.map
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine


class BattleYokodzunsEditor(
    private val battle: Battle
) : EditBattleYokodzunsCallback {

    private val selectedYokodzunsIdsProducer =
        ActualProducerSimple<List<String>>(battle.yokodzunsIds)

    val selectedYokodzunsIds: List<String>
        get() = selectedYokodzunsIdsProducer.currentState

    val selectedYokodzuns = Producer.combine(
        producer1 = YokodzunsDataManager,
        producer2 = selectedYokodzunsIdsProducer
    ) { yokodzuns, selectedYokodzuns ->
        yokodzuns.map { yokodzunsList ->
            yokodzunsList.filter { it.id in selectedYokodzuns }
        }
    }

    fun invalidate() = YokodzunsDataManager.invalidate()

    fun selectNewYokodzun() = AppActivityConnector.showLayer({ context ->
        SelectYokodzunForBattleLayer.newInstance(
            context = context,
            alreadySelectedYokodzunsIds = selectedYokodzunsIds,
            callback = this@BattleYokodzunsEditor
        )
    })

    fun createAdditionalButtonInfo(
        yokodzun: Yokodzun
    ) = AdditionalButton.Info(
        icon = DrawableGetter(R.drawable.ic_remove_fg),
        color = ColorManager.RED_TRIPLE
    ) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.battle_yokodzuns_layer_remove_confirm_dialog_title),
            text = StringGetter(R.string.battle_yokodzuns_layer_remove_confirm_dialog_text),
            confirmText = StringGetter(R.string.dialog_remove)
        ) {
            selectedYokodzunsIdsProducer.updateState(selectedYokodzunsIdsProducer.currentState - yokodzun.id)
        }
    }

    override fun addYokodzun(yokodzunId: String) =
        selectedYokodzunsIdsProducer.updateState(selectedYokodzunsIdsProducer.currentState + yokodzunId)

}