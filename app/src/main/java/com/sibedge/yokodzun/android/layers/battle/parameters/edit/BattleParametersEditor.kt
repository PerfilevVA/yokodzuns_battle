package com.sibedge.parameter.android.layers.battle.parameters.edit

import com.sibedge.parameter.android.layers.select.SelectParameterForBattleLayer
import com.sibedge.parameter.android.data.ParametersDataManager
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.battle.parameters.item.BattleFullParameter
import com.sibedge.yokodzun.android.layers.battle.parameters.edit.EditBattleParametersCallback
import com.sibedge.yokodzun.android.ui.view.plus_minus.PlusMinusColumnInfo
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleParameter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.jutils.getter.base.map
import ru.hnau.jutils.handle
import ru.hnau.jutils.ifFalse
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine


class BattleParametersEditor(
    private val battle: Battle
) : EditBattleParametersCallback {

    private val selectedParametersProducer =
        ActualProducerSimple<List<BattleParameter>>(battle.parameters)

    val alreadySelectedParameters: List<BattleParameter>
        get() = selectedParametersProducer.currentState

    val selectedParameters = Producer.combine(
        producer1 = ParametersDataManager,
        producer2 = selectedParametersProducer
    ) { parameters, selectedParameters ->
        val selectedParametersIds = selectedParameters.map { it.parameterId }
        parameters.map { parametersList ->
            parametersList.mapNotNull { parameter ->
                (parameter.id in selectedParametersIds).ifFalse { return@mapNotNull null }
                BattleFullParameter(
                    parameter = parameter,
                    weight = selectedParameters.find { it.parameterId == parameter.id }?.weight ?: 1
                )
            }
        }
    }

    fun invalidate() = ParametersDataManager.invalidate()

    fun selectNewParameter() = AppActivityConnector.showLayer({ context ->
        SelectParameterForBattleLayer.newInstance(
            context = context,
            alreadySelectedParametersIds = alreadySelectedParameters.map { it.parameterId },
            callback = this@BattleParametersEditor
        )
    })

    fun onParameterCick(parameter: BattleFullParameter) {
        AppActivityConnector.showBottomSheet {
            title(parameter.parameter.entityNameWithTitle)
            closeItem(StringGetter(R.string.battle_parameters_layer_action_chanhe_weight)) {
                changeWeight(parameter)
            }
            closeItem(StringGetter(R.string.battle_parameters_layer_action_remove)) {
                askAndRemove(parameter)
            }
        }
    }

    private fun changeWeight(
        parameter: BattleFullParameter
    ) = AppActivityConnector.showPlusMinusDialog(
        title = StringGetter(R.string.battle_parameters_layer_change_weight_title),
        text = StringGetter(R.string.battle_parameters_layer_change_weight_text),
        confirmButtonText = StringGetter(R.string.dialog_save),
        initialValue = parameter.weight,
        valueToStringConverter = { it.toString().toGetter() },
        columns = listOf(
            PlusMinusColumnInfo(
                title = "10".toString().toGetter(),
                actionMinus = { it - 10 },
                actionPlus = { it + 10 }
            ),
            PlusMinusColumnInfo(
                title = "1".toString().toGetter(),
                actionMinus = { it - 1 },
                actionPlus = { it + 1 }
            )
        ),
        availableValueRange = 1..100
    ) { weight ->
        updateSelectedParameters {
            map {
                (it.parameterId == parameter.id).handle(
                    onTrue = { it.copy(weight = weight) },
                    onFalse = { it }
                )
            }
        }
        return@showPlusMinusDialog true
    }


    private fun askAndRemove(
        parameter: BattleFullParameter
    ) = AppActivityConnector.showConfirmDialog(
        title = StringGetter(R.string.battle_parameters_layer_remove_confirm_dialog_title),
        text = StringGetter(R.string.battle_parameters_layer_remove_confirm_dialog_text),
        confirmText = StringGetter(R.string.dialog_remove)
    ) {
        updateSelectedParameters { filter { it.parameterId != parameter.id } }
    }

    override fun addParameter(parameterId: String) {
        val newParameter = BattleParameter(parameterId = parameterId, weight = 1)
        updateSelectedParameters { this + newParameter }
    }

    private fun updateSelectedParameters(
        update: List<BattleParameter>.() -> List<BattleParameter>
    ) = selectedParametersProducer.updateState(
        state = selectedParametersProducer.currentState.update()
    )

}