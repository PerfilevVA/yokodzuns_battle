package com.sibedge.yokodzun.android.utils.battle.action

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object BattleActionEditParameters : BattleAction(
    title = StringGetter(R.string.battle_action_edit_parameters)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) {
        //TODO
    }

}