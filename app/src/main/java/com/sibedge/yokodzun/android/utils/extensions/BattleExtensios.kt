package com.sibedge.yokodzun.android.utils.extensions

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import ru.hnau.androidutils.context_getters.StringGetter


val Battle.sortKey: String
    get() {
        val statusKey = when (status) {
            BattleStatus.IN_PROGRESS -> 0
            BattleStatus.BEFORE -> 1
            BattleStatus.AFTER -> 2
        }
        val createdKey = Long.MAX_VALUE - created
        return "$statusKey-$createdKey"
    }

val Battle.entityNameWithTitle
    get() = StringGetter(R.string.entity_battle) + " ${description.title}"

val BattleStatus.title
    get() = when (this) {
        BattleStatus.BEFORE -> StringGetter(R.string.battle_view_status_before)
        BattleStatus.IN_PROGRESS -> StringGetter(R.string.battle_view_status_in_progress)
        BattleStatus.AFTER -> StringGetter(R.string.battle_view_status_after)
    }

val BattleStatus.color
    get() = when (this) {
        BattleStatus.IN_PROGRESS -> ColorManager.GREEN_TRIPLE.main
        else -> ColorManager.PRIMARY
    }