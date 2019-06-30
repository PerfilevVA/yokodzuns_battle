package com.sibedge.yokodzun.android.utils.battle.action

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.admin.AdminAllBattlesDataManager
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object BattleActionRemove : BattleAction(
    title = StringGetter(R.string.battle_action_remove)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showConfirmDialog(
        title = StringGetter(R.string.battle_action_remove_confirm_dialog_title),
        text = StringGetter(R.string.battle_action_remove_confirm_dialog_text),
        confirmText = StringGetter(R.string.dialog_remove)
    ) {
        coroutinesExecutor { AdminAllBattlesDataManager.remove(battleId = battle.id) }
    }

}