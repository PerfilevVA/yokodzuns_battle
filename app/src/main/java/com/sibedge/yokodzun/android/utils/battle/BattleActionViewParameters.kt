package com.sibedge.yokodzun.android.utils.battle

import com.sibedge.parameter.android.layers.battle.parameters.ImmutableBattleParametersLayer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object BattleActionViewParameters : BattleAction(
    title = StringGetter(R.string.battle_action_view_parameters)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showLayer({ context ->
        ImmutableBattleParametersLayer.newInstance(context, battle)
    })

}