package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.common.data.battle.Battle


object RaterBattleDataManager : YDataManager<Battle>() {

    override suspend fun getValue() =
        API.battleForRater().await()

}