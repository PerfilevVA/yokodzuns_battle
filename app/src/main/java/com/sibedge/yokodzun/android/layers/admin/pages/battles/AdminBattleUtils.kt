package com.sibedge.yokodzun.android.layers.admin.pages.battles

import com.sibedge.yokodzun.android.utils.battle.*
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import kotlinx.coroutines.CoroutineScope
import ru.hnau.jutils.handle


object AdminBattleUtils {

    private val ACTIONS = run {
        val actionsList = arrayOf(
            BattleActionStart to listOf(BattleStatus.BEFORE),
            BattleActionStop to listOf(BattleStatus.IN_PROGRESS),
            BattleActionViewRates to listOf(BattleStatus.IN_PROGRESS, BattleStatus.AFTER),
            BattleActionViewSectionsRates to listOf(BattleStatus.IN_PROGRESS, BattleStatus.AFTER),
            BattleActionEditDescription to listOf(BattleStatus.BEFORE),
            BattleActionEditParameters to listOf(BattleStatus.BEFORE),
            BattleActionEditYokodzuns to listOf(BattleStatus.BEFORE),
            BattleActionEditSections to listOf(BattleStatus.BEFORE),
            BattleActionEditRaters to listOf(BattleStatus.BEFORE, BattleStatus.IN_PROGRESS),
            BattleActionRemove to listOf(BattleStatus.BEFORE, BattleStatus.AFTER)
        )
        BattleStatus.values().associate { status ->
            val actionsForStatus = actionsList.filter { status in it.second }.map { it.first }
            status to actionsForStatus
        }
    }


    fun showBattleActions(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showBottomSheet {
        title(battle.entityNameWithTitle)
        val actions = ACTIONS.getValue(battle.status)
        actions.forEach { action ->
            closeItem(action.title) { action.execute(battle, coroutinesExecutor) }
        }
    }

    fun onParametersClicked(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = (battle.status == BattleStatus.BEFORE).handle(
        forTrue = BattleActionEditParameters,
        forFalse = BattleActionViewParameters
    ).execute(
        battle = battle,
        coroutinesExecutor = coroutinesExecutor
    )

    fun onYokodzunsClicked(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = (battle.status == BattleStatus.BEFORE).handle(
        forTrue = BattleActionEditYokodzuns,
        forFalse = BattleActionViewYokodzuns
    ).execute(
        battle = battle,
        coroutinesExecutor = coroutinesExecutor
    )

    fun onSectionsClicked(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = (battle.status == BattleStatus.BEFORE).handle(
        forTrue = BattleActionEditSections,
        forFalse = BattleActionViewSections
    ).execute(
        battle = battle,
        coroutinesExecutor = coroutinesExecutor
    )

}