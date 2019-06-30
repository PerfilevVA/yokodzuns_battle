package com.sibedge.yokodzun.android.ui.cell.action

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object BattleActionViewYokodzuns : BattleAction(
    title = StringGetter(R.string.battle_action_view_yokodzuns)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) {
        //TODO
    }

}