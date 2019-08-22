package com.sibedge.yokodzun.android.layers.rate.list.item

import com.sibedge.yokodzun.android.data.RaterRatesDataManager
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.Team


class RatesListItem private constructor(
    val type: RatesListItemType,
    val team: Team? = null,
    val parameter: Parameter? = null,
    val rate: Pair<RaterRatesDataManager.Key, Float?>? = null
) {

    companion object {

        fun createTeamTitle(team: Team) =
            RatesListItem(type = RatesListItemType.TEAM_TITLE, team = team)

        fun createParameterTitle(parameter: Parameter) =
            RatesListItem(type = RatesListItemType.PARAMETER_TITLE, parameter = parameter)

        fun createRateItem(key: RaterRatesDataManager.Key, value: Float?) =
            RatesListItem(type = RatesListItemType.RATE_ITEM, rate = Pair(key, value))

    }

    val itemTypeKey get() = type.key


}