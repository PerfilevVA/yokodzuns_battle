package com.sibedge.yokodzun.android.layers.battle.teams.edit

import android.content.Context
import android.view.ViewGroup
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.AdminBattlesDataManager
import com.sibedge.yokodzun.android.layers.battle.teams.BattleTeamsLayer
import com.sibedge.yokodzun.android.ui.view.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncViewsWithContentListContainer
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.Team
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.jutils.producer.extensions.not


class EditBattleTeamsLayer(
    context: Context
) : BattleTeamsLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = EditBattleTeamsLayer(context).apply {
            this.battle = battle
            this.editor = BattleTeamsEditor(battle)
        }

    }

    @LayerState
    override lateinit var battle: Battle

    @LayerState
    private lateinit var editor: BattleTeamsEditor

    override val teamsProducer
        get() = editor.selectedTeams

    override fun invalidateTeams() = editor.invalidate()

    override val onEmptyListInfoView by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.battle_teams_layer_no_teams_title),
            button = StringGetter(R.string.battle_teams_layer_no_teams_add_team) to editor::selectNewTeam
        )
    }

    override val additionalButtonInfo by lazy {
        { team: Team -> editor.createAdditionalButtonInfo(team) }
    }

    override fun ViewGroup.configureView(listView: AsyncViewsWithContentListContainer<Team>) {
        addPrimaryActionButton(
            icon = DrawableGetter(R.drawable.ic_add_fg),
            title = StringGetter(R.string.battle_teams_layer_no_teams_add_team),
            needShowTitle = listView.onListScrolledToTopProducer.not(),
            onClick = editor::selectNewTeam
        ) {
            applyFrameParams {
                setMargins(SizeManager.DEFAULT_SEPARATION)
                setEndBottomGravity()
            }
        }
    }

    override fun handleGoBack(): Boolean {
        uiJobLocked {
            AdminBattlesDataManager.updateTeamsIds(
                battleId = battle.id,
                teamsIds = editor.selectedTeamsIds
            )
            managerConnector.goBack()
        }
        return true
    }

}