package com.sibedge.yokodzun.android.api

import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.Rate
import com.sibedge.yokodzun.common.data.Team
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleParameter
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description
import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface YService {

    @PATCH("/admin/login")
    fun adminLogin(
        @Query("password") password: String,
        @Query("app-instance-uuid") appInstanceUUID: String
    ): Deferred<String>

    @PATCH("/rater/login")
    fun raterLogin(
        @Query("app-instance-uuid") appInstanceUUID: String
    ): Deferred<Unit>

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
    fun updateBattleTeamsIds(
        @Path("battle-id") battleId: String,
        @Body teamsIds: List<String>
    ): Deferred<Unit>

    @GET("/yokodzuns")
    fun getAllTeams(
    ): Deferred<List<Team>>

    @PUT("/yokodzuns")
    fun createNewTeam(
    ): Deferred<String>

    @DELETE("/yokodzuns/{team-id}/remove")
    fun removeTeam(
        @Path("team-id") teamId: String
    ): Deferred<Unit>

    @PATCH("/yokodzuns/{team-id}/description")
    fun updateTeamDescription(
        @Path("team-id") teamId: String,
        @Body description: Description
    ): Deferred<Unit>

    @GET("/parameters")
    fun getAllParameters(
    ): Deferred<List<Parameter>>

    @PUT("/parameters")
    fun createNewParameter(
    ): Deferred<String>

    @DELETE("/parameters/{parameter-id}/remove")
    fun removeParameter(
        @Path("parameter-id") parameterId: String
    ): Deferred<Unit>

    @PATCH("/parameters/{parameter-id}/description")
    fun updateParameterDescription(
        @Path("parameter-id") parameterId: String,
        @Body description: Description
    ): Deferred<Unit>

    @GET("/raters")
    fun ratersGetForBattle(
        @Query("battle-id") battleId: String
    ): Deferred<List<String>>

    @PUT("/raters")
    fun ratersAdd(
        @Query("battle-id") battleId: String,
        @Query("count") count: Int
    ): Deferred<List<String>>

    @DELETE("/raters/{rater-code}")
    fun raterRemove(
        @Path("rater-code") raterCode: String
    ): Deferred<Unit>

    @GET("/battle/for-rater")
    fun battleForRater(
    ): Deferred<Battle>

    @GET("/rater/rates")
    fun getRaterRates(
    ): Deferred<List<Rate>>

    @PUT("/battle/{battle-id}/section/{section-id}/parameter/{parameter-id}/team/{team-id}/rate")
    fun rate(
        @Path("battle-id") battleId: String,
        @Path("section-id") sectionId: String,
        @Path("parameter-id") parameterId: String,
        @Path("team-id") teamId: String,
        @Query("value") value: Float
    ): Deferred<Unit>

    @GET("/battles/{battle-id}/rates")
    fun getBattleRates(
        @Path("battle-id") battleId: String
    ): Deferred<List<Rate>>

    @PATCH("/client-app-instance/{app-instance-uuid}/push-token/{push-token}")
    fun changePushToken(
        @Path("app-instance-uuid") appInstanceUUID: String,
        @Path("push-token") pushToken: String
    ): Deferred<Unit>

    @PATCH("/on-logout")
    fun onLogout(
        @Query("app-instance-uuid") appInstanceUUID: String
    ): Deferred<Unit>

    @PUT("/battles/{battle-id}/raters-message")
    fun sendMessageToRaters(
        @Path("battle-id") battleId: String,
        @Query("message") message: String
    ): Deferred<Unit>

}