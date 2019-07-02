package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.common.data.Rate
import ru.hnau.jutils.cache.AutoCache


class BattleRatesDataManager(
    private val battleId: String
) : YDataManager<List<Rate>>() {

    companion object : AutoCache<String, BattleRatesDataManager>(
        capacity = 1024,
        getter = ::BattleRatesDataManager
    )

    override suspend fun getValue() =
        API.getBattleRates(battleId).await()

}