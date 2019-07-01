package com.sibedge.parameter.android.layers.battle.parameters

import android.content.Context
import com.sibedge.parameter.android.data.ParametersDataManager
import com.sibedge.yokodzun.android.layers.battle.parameters.item.BattleFullParameter
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.getter.base.map
import ru.hnau.jutils.ifFalse
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine


class ImmutableBattleParametersLayer(
    context: Context
) : BattleParametersLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = ImmutableBattleParametersLayer(context).apply {
            this.battle = battle
        }

    }

    @LayerState
    override lateinit var battle: Battle

    override val parametersProducer =
        ParametersDataManager.map { parametersGetter ->
            val selectedParametersIds = battle.parameters.map { it.id }
            parametersGetter.map { parametersList ->
                parametersList.mapNotNull { parameter ->
                    (parameter.id in selectedParametersIds).ifFalse { return@mapNotNull null }
                    BattleFullParameter(
                        parameter = parameter,
                        weight = battle.parameters.find { it.parameterId == parameter.id }?.weight
                            ?: 1
                    )
                }
            }
        }

    override fun invalidateBattleFullParameters() =
        ParametersDataManager.invalidate()

}