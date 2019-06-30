package com.sibedge.yokodzun.android.ui.cell.action

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.description.EditBattleDescriptionLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object BattleActionEditDescription : BattleAction(
    title = StringGetter(R.string.battle_action_edit_description)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) {
        AppActivityConnector.showLayer({ context ->
            EditBattleDescriptionLayer.newInstance(context, battle)
        })
    }

}