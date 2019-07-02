package com.sibedge.yokodzun.android.utils.battle

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.raters.BattleRatersLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object BattleActionEditRaters : BattleAction(
    title = StringGetter(R.string.battle_action_edit_raters)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showLayer({ context ->
        BattleRatersLayer.newInstance(context, battle)
    })

}