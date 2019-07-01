package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.BattleParameter
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description


object YokodzunsDataManager : YListDataManager<String, String, Yokodzun>() {

    override suspend fun getList() =
        API.getAllYokodzuns().await()

    suspend fun createNew() {
        val yokodzunId = API.createNewYokodzun().await()
        val yokodzun = Yokodzun(id = yokodzunId)
        insertItem(yokodzun)
    }

    suspend fun remove(
        yokodzunId: String
    ) {
        API.removeYokodzun(yokodzunId).await()
        removeItem(yokodzunId)
    }

    suspend fun updateDescription(
        yokodzunId: String,
        description: Description
    ) {
        API.updateYokodzunDescription(yokodzunId, description).await()
        updateOrInvalidateItem(yokodzunId) { copy(description = description) }
    }

}