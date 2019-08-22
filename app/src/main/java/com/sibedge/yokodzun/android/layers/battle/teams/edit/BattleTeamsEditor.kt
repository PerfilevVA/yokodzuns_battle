package com.sibedge.yokodzun.android.layers.battle.teams.edit

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.TeamsDataManager
import com.sibedge.yokodzun.android.layers.select.SelectTeamForBattleLayer
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.Team
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.jutils.getter.base.map
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine


class BattleTeamsEditor(
    private val battle: Battle
) : EditBattleTeamsCallback {

    private val selectedTeamsIdsProducer =
        ActualProducerSimple<List<String>>(battle.teamsIds)

    val selectedTeamsIds: List<String>
        get() = selectedTeamsIdsProducer.currentState

    val selectedTeams = Producer.combine(
        producer1 = TeamsDataManager,
        producer2 = selectedTeamsIdsProducer
    ) { teams, selectedTeams ->
        teams.map { teamsList ->
            teamsList.filter { it.id in selectedTeams }
        }
    }

    fun invalidate() = TeamsDataManager.invalidate()

    fun selectNewTeam() = AppActivityConnector.showLayer({ context ->
        SelectTeamForBattleLayer.newInstance(
            context = context,
            alreadySelectedTeamsIds = selectedTeamsIds,
            callback = this@BattleTeamsEditor
        )
    })

    fun createAdditionalButtonInfo(
        team: Team
    ) = AdditionalButton.Info(
        icon = DrawableGetter(R.drawable.ic_remove_fg),
        color = ColorManager.RED_TRIPLE
    ) {
        AppActivityConnector.showConfirmDialog(
            title = StringGetter(R.string.battle_teams_layer_remove_confirm_dialog_title),
            text = StringGetter(R.string.battle_teams_layer_remove_confirm_dialog_text),
            confirmText = StringGetter(R.string.dialog_remove)
        ) {
            selectedTeamsIdsProducer.updateState(selectedTeamsIdsProducer.currentState - team.id)
        }
    }

    override fun addTeam(teamId: String) =
        selectedTeamsIdsProducer.updateState(selectedTeamsIdsProducer.currentState + teamId)

}