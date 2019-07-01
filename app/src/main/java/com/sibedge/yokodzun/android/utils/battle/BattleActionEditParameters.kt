package com.sibedge.yokodzun.android.utils.battle

import com.sibedge.parameter.android.layers.battle.parameters.edit.EditBattleParametersLayer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.battle.yokodzuns.edit.EditBattleYokodzunsLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object BattleActionEditParameters : BattleAction(
    title = StringGetter(R.string.battle_action_edit_parameters)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showLayer({
        EditBattleParametersLayer.newInstance(it, battle)
    })

}