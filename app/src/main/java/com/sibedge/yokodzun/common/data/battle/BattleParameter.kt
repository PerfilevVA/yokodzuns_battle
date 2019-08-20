package com.sibedge.yokodzun.common.data.battle

import com.sibedge.yokodzun.common.data.helpers.ListItem


data class BattleParameter(
        val parameterId: String = "",
        val weight: Int = 1
) : ListItem<String, String> {

    override val id get() = parameterId
    override val sortKey get() = parameterId

}