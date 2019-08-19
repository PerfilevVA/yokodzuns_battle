package com.sibedge.yokodzun.android.layers.rate.list.item

import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.Rate
import com.sibedge.yokodzun.common.data.Yokodzun


class RatesListItem private constructor(
    val type: RatesListItemType,
    val yokodzun: Yokodzun? = null,
    val parameter: Parameter? = null,
    val rate: Rate? = null
) {

    companion object {

        fun createYokodzunTitle(yokodzun: Yokodzun) =
            RatesListItem(type = RatesListItemType.YOKODZUN_TITLE, yokodzun = yokodzun)

        fun createParameterTitle(parameter: Parameter) =
            RatesListItem(type = RatesListItemType.PARAMETER_TITLE, parameter = parameter)

        fun createRateItem(rate: Rate?) =
            RatesListItem(type = RatesListItemType.RATE_ITEM, rate = rate)

    }

    val itemTypeKey get() = type.key


}