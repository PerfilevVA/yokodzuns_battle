package com.sibedge.yokodzun.android.api

import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleParameter
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description
import com.sibedge.yokodzun.common.utils.Validators
import kotlinx.coroutines.Deferred
import okhttp3.internal.http.HttpHeaders
import retrofit2.http.*
import ru.hnau.jutils.possible.Possible


interface RTService {

    @PATCH("/admin/login")
    fun adminLogin(
        @Query("password") password: String
    ): Deferred<String>

    @PATCH("/admin/change-password")
    fun adminChangePassword(
        @Query("old-password") oldPassword: String,
        @Query("new-password") newPassword: String
    ): Deferred<Unit>

    @GET("/battles")
    fun getAllBattles(
    ): Deferred<List<Battle>>

    @PUT("/battles")
    fun createNewBattle(
    ): Deferred<Battle>

    @PATCH("/battles/{battle-id}/start")
    fun startBattle(
        @Path("battle-id") battleId: String
    ): Deferred<Unit>

    @PATCH("/battles/{battle-id}/stop")
    fun stopBattle(
        @Path("battle-id") battleId: String
    ): Deferred<Unit>

    @DELETE("/battles/{battle-id}/remove")
    fun removeBattle(
        @Path("battle-id") battleId: String
    ): Deferred<Unit>

    @PATCH("/battles/{battle-id}/description")
    fun updateBattleDescription(
        @Path("battle-id") battleId: String,
        @Body description: Description
    ): Deferred<Unit>

    @PATCH("/battles/{battle-id}/sections")
    fun updateBattleSections(
        @Path("battle-id") battleId: String,
        @Body sections: List<Section>
    ): Deferred<Unit>

    @PATCH("/battles/{battle-id}/parameters")
    fun updateBattleParameters(
        @Path("battle-id") battleId: String,
        @Body parameters: List<BattleParameter>
    ): Deferred<Unit>

    @PATCH("/battles/{battle-id}/yokodzuns-ids")
    fun updateBattleYokodzunsIds(
        @Path("battle-id") battleId: String,
        @Body yokodzunsIds: List<String>
    ): Deferred<Unit>

}