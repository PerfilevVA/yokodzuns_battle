package com.sibedge.yokodzun.android.ui.cell.action

import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


abstract class BattleAction(
    val title: StringGetter
) {

    abstract fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    )

}