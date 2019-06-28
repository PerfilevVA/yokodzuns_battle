package com.sibedge.yokodzun.android.ui.cell

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.cell.BattleViewUtils.createParametersCountView
import com.sibedge.yokodzun.android.ui.cell.BattleViewUtils.createSectionsCountView
import com.sibedge.yokodzun.android.ui.cell.BattleViewUtils.createYokodzunsCountView
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


class AdminBattleView(
    context: Context,
    coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
) : BattleView(
    context = context,
    onClick = { AdminBattleViewUtils.onClick(it, coroutinesExecutor) },
    onParametersCountClicked = AdminBattleViewUtils::editBattleParameters,
    onSectionsCountClicked = AdminBattleViewUtils::editBattleSectios,
    onYoconzunsCountClicked = AdminBattleViewUtils::editBattleYokodzuns
)