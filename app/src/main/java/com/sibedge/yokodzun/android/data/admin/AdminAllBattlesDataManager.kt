package com.sibedge.yokodzun.android.data.admin

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.android.data.YDataManager
import com.sibedge.yokodzun.android.utils.extensions.sortKey
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleParameter
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.jutils.handle


object AdminAllBattlesDataManager : YDataManager<List<Battle>>() {

    override suspend fun getValue() =
        API.getAllBattles().await().sortedBy { it.sortKey }

    suspend fun createNew() {
        val battle = API.createNewBattle().await()
        updateOrInvalidate { oldBattles ->
            (oldBattles + battle).sortedBy { it.sortKey }
        }
    }

    private inline fun updateBattleOrInvalidate(
        battleId: String,
        crossinline update: Battle.() -> Battle
    ) = updateOrInvalidate {
        it.map {
            (it.id == battleId).handle(
                onTrue = { it.update() },
                onFalse = { it }
            )
        }.sortedBy { it.sortKey }
    }

    suspend fun start(
        battleId: String
    ) {
        API.startBattle(battleId).await()
        updateBattleOrInvalidate(battleId) { copy(status = BattleStatus.IN_PROGRESS) }
    }

    suspend fun stop(
        battleId: String
    ) {
        API.stopBattle(battleId).await()
        updateBattleOrInvalidate(battleId) { copy(status = BattleStatus.AFTER) }
    }

    suspend fun remove(
        battleId: String
    ) {
        API.removeBattle(battleId).await()
        updateOrInvalidate { it.filter { it.id != battleId } }
    }

    suspend fun updateDescription(
        battleId: String,
        description: Description
    ) {
        API.updateBattleDescription(battleId, description).await()
        updateBattleOrInvalidate(battleId) { copy(description = description) }
    }

    suspend fun updateSections(
        battleId: String,
        sections: List<Section>
    ) {
        API.updateBattleSections(battleId, sections).await()
        updateBattleOrInvalidate(battleId) { copy(sections = sections) }
    }

    suspend fun updateParameters(
        battleId: String,
        parameters: List<BattleParameter>
    ) {
        API.updateBattleParameters(battleId, parameters).await()
        updateBattleOrInvalidate(battleId) { copy(parameters = parameters) }
    }

    suspend fun updateYokodzunsIds(
        battleId: String,
        yokodzunsIds: List<String>
    ) {
        API.updateBattleYokodzunsIds(battleId, yokodzunsIds).await()
        updateBattleOrInvalidate(battleId) { copy(yokodzunsIds = yokodzunsIds) }
    }

}