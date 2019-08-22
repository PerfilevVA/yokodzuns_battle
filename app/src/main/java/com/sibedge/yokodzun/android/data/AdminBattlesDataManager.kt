package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleParameter
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.jutils.ifTrue


object AdminBattlesDataManager : YListDataManager<String, String, Battle>() {

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
        val currentDescription = existenceValue?.find { it.id == battleId }?.description
        (currentDescription == description).ifTrue { return }
        API.updateBattleDescription(battleId, description).await()
        updateOrInvalidateItem(battleId) { copy(description = description) }
    }

    suspend fun updateSections(
        battleId: String,
        sections: List<Section>
    ) {
        val sortedSections = sections.sortedBy { it.sortKey }
        val currentSections = existenceValue?.find { it.id == battleId }?.sections
        (currentSections == sortedSections).ifTrue { return }
        API.updateBattleSections(battleId, sortedSections).await()
        updateOrInvalidateItem(battleId) { copy(sections = sortedSections) }
    }

    suspend fun updateParameters(
        battleId: String,
        parameters: List<BattleParameter>
    ) {
        val sortedParameters = parameters.sortedBy { it.sortKey }
        val currentParameter = existenceValue?.find { it.id == battleId }?.parameters
        (currentParameter == sortedParameters).ifTrue { return }
        API.updateBattleParameters(battleId, sortedParameters).await()
        updateOrInvalidateItem(battleId) { copy(parameters = sortedParameters) }
    }

    suspend fun updateTeamsIds(
        battleId: String,
        teamsIds: List<String>
    ) {
        val sortedTeamsIds = teamsIds.sorted()
        val currentTeamsIds = existenceValue?.find { it.id == battleId }?.teamsIds
        (currentTeamsIds == sortedTeamsIds).ifTrue { return }
        API.updateBattleTeamsIds(battleId, sortedTeamsIds).await()
        updateOrInvalidateItem(battleId) { copy(teamsIds = sortedTeamsIds) }
    }

}