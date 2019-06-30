package com.sibedge.yokodzun.android.ui.view.cell

import android.content.Context
import com.sibedge.yokodzun.android.utils.battle.AdminBattleUtils
import kotlinx.coroutines.CoroutineScope


class AdminBattleView(
    context: Context,
    coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
) : BattleView(
    context = context,
    onClick = {
        AdminBattleUtils.showBattleActions(
            it,
            coroutinesExecutor
        )
    },
    onParametersCountClicked = {
        AdminBattleUtils.onParametersClicked(
            it,
            coroutinesExecutor
        )
    },
    onSectionsCountClicked = {
        AdminBattleUtils.onSectionsClicked(
            it,
            coroutinesExecutor
        )
    },
    onYoconzunsCountClicked = {
        AdminBattleUtils.onYokodzunsClicked(
            it,
            coroutinesExecutor
        )
    }
)