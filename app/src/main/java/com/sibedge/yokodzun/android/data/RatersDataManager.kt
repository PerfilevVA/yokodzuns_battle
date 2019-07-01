package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import ru.hnau.jutils.cache.AutoCache


class RatersDataManager(
    private val battleId: String
) : YDataManager<List<String>>() {

    companion object : AutoCache<String, RatersDataManager>(
        capacity = 1024,
        getter = ::RatersDataManager
    )

    override suspend fun getValue() =
        API.ratersGetForBattle(battleId).await().sorted()

    suspend fun add(count: Int) {
        val newRaters = API.ratersAdd(battleId, count).await()
        updateOrInvalidate { oldRaters -> (oldRaters + newRaters).sorted() }
    }

    suspend fun remove(code: String) {
        API.raterRemove(code).await()
        updateOrInvalidate { raters -> raters.filter { it != code } }
    }

}