package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import ru.hnau.jutils.ifTrue


object RaterRatesDataManager : YDataManager<Map<RaterRatesDataManager.Key, Float>>() {

    override suspend fun getValue() =
        API.getRaterRates().await().associate { (battleId, sectionId, yokodzunId, parameterId, _, value) ->
            Key(battleId, sectionId, yokodzunId, parameterId) to value
        }

    data class Key(
        val battleId: String,
        val sectionId: String,
        val yokodzunId: String,
        val parameterId: String
    )

    suspend fun rate(
        key: Key,
        value: Float
    ) {
        val oldValue = existenceValue?.get(key)
        (oldValue == value).ifTrue { return }
        API.rate(
            battleId = key.battleId,
            sectionId = key.sectionId,
            yokodzunId = key.yokodzunId,
            parameterId = key.parameterId,
            value = value
        ).await()
        updateOrInvalidate { it.toMutableMap().apply { set(key, value) } }
    }

    suspend fun sync() {
        UnsyncRatesContainer.items.forEach { rate ->
            rate(rate.key, rate.value)
        }
        UnsyncRatesContainer.clear()
    }

    object UnsyncRatesContainer {

        val items = HashMap<Key, Float>()

        fun put(key: Key, value: Float) = items.put(key, value)

        fun clear() = items.clear()
    }
}