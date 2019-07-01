package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleParameter
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.jutils.ifTrue


object BattlesDataManager : YListDataManager<String, String, Battle>() {

    override suspend fun getList() =
        API.getAllBattles().await()

    suspend fun createNew() {
        val battle = API.createNewBattle().await()
        insertItem(battle)
    }

    suspend fun start(
        battleId: String
    ) {
        API.startBattle(battleId).await()
        updateOrInvalidateItem(battleId) { copy(status = BattleStatus.IN_PROGRESS) }
    }

    suspend fun stop(
        battleId: String
    ) {
        API.stopBattle(battleId).await()
        updateOrInvalidateItem(battleId) { copy(status = BattleStatus.AFTER) }
    }

    suspend fun remove(
        battleId: String
    ) {
        API.removeBattle(battleId).await()
        removeItem(battleId)
    }

    suspend fun updateDescription(
        battleId: String,
        description: Description
    ) {
        API.updateBattleDescription(battleId, description).await()
        updateOrInvalidateItem(battleId) { copy(description = description) }
    }

    suspend fun updateSections(
        battleId: String,
        sections: List<Section>
    ) {
        val currentSections = existenceValue?.find { it.id == battleId }?.sections
        (currentSections == sections).ifTrue { return }
        API.updateBattleSections(battleId, sections).await()
        updateOrInvalidateItem(battleId) { copy(sections = sections) }
    }

    suspend fun updateParameters(
        battleId: String,
        parameters: List<BattleParameter>
    ) {
        API.updateBattleParameters(battleId, parameters).await()
        updateOrInvalidateItem(battleId) { copy(parameters = parameters) }
    }

    suspend fun updateYokodzunsIds(
        battleId: String,
        yokodzunsIds: List<String>
    ) {
        API.updateBattleYokodzunsIds(battleId, yokodzunsIds).await()
        updateOrInvalidateItem(battleId) { copy(yokodzunsIds = yokodzunsIds) }
    }

}