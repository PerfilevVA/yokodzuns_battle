package com.sibedge.yokodzun.android.utils.battle

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.rates.BattleRatesLayer
import com.sibedge.yokodzun.android.layers.sections.ViewSectionsLayer
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object BattleActionViewRates : BattleAction(
    title = StringGetter(R.string.battle_action_view_rates)
) {

    override fun execute(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showLayer({ context ->
        BattleRatesLayer.newInstance(context, battle)
    })

}