package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.android.api.API
import com.sibedge.yokodzun.common.data.Team
import com.sibedge.yokodzun.common.data.helpers.Description


object TeamsDataManager : YListDataManager<String, String, Team>() {

    override suspend fun getList() =
        API.getAllTeams().await()

    suspend fun createNew() {
        val teamId = API.createNewTeam().await()
        val team = Team(id = teamId)
        insertItem(team)
    }

    suspend fun remove(
        teamId: String
    ) {
        API.removeTeam(teamId).await()
        removeItem(teamId)
    }

    suspend fun updateDescription(
        teamId: String,
        description: Description
    ) {
        API.updateTeamDescription(teamId, description).await()
        updateOrInvalidateItem(teamId) { copy(description = description) }
    }

}