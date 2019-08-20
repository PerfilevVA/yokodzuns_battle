package com.sibedge.yokodzun.common.data.battle

import com.sibedge.yokodzun.common.data.helpers.ListItem
import com.sibedge.yokodzun.common.data.helpers.Description


data class Battle(
        override val id: String = "",
        val created: Long = 0,
        val description: Description = Description(),
        val sections: List<Section> = emptyList(),
        val parameters: List<BattleParameter> = emptyList(),
        val yokodzunsIds: List<String> = emptyList(),
        val status: BattleStatus = BattleStatus.BEFORE
) : ListItem<String, String> {

    override val sortKey: String
        get() {
            val statusKey = when (status) {
                BattleStatus.IN_PROGRESS -> 0
                BattleStatus.BEFORE -> 1
                BattleStatus.AFTER -> 2
            }
            val createdKey = Long.MAX_VALUE - created
            return "$statusKey-$createdKey"
        }

}