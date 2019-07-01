package com.sibedge.yokodzun.android.utils.battle

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.BattlesDataManager
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object BattleActionStop : BattleAction(
    title = StringGetter(R.string.battle_action_stop)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showConfirmDialog(
        title = StringGetter(R.string.battle_action_stop_confirm_dialog_title),
        text = StringGetter(R.string.battle_action_stop_confirm_dialog_text),
        confirmText = StringGetter(R.string.battle_action_stop_confirm_dialog_button)
    ) {
        coroutinesExecutor { BattlesDataManager.stop(battleId = battle.id) }
    }

}