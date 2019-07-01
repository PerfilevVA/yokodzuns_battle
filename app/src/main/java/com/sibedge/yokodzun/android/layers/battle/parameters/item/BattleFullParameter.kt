package com.sibedge.yokodzun.android.layers.battle.parameters.item

import com.sibedge.yokodzun.common.data.Parameter


data class BattleFullParameter(
    val parameter: Parameter,
    val weight: Int
) {

    val id get() = parameter.id

}