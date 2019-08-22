package com.sibedge.yokodzun.android.layers.select

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.TeamsDataManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.battle.teams.edit.EditBattleTeamsCallback
import com.sibedge.yokodzun.android.ui.view.cell.TeamView
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncViewsWithContentListContainer
import com.sibedge.yokodzun.common.data.Team
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.base.map


class SelectTeamForBattleLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            alreadySelectedTeamsIds: List<String>,
            callback: EditBattleTeamsCallback
        ) = SelectTeamForBattleLayer(context).apply {
            this.callback = callback
            this.alreadySelectedTeamsIds = alreadySelectedTeamsIds
        }

    }

    @LayerState
    private lateinit var callback: EditBattleTeamsCallback

    @LayerState
    private lateinit var alreadySelectedTeamsIds: List<String>

    override val title = StringGetter(R.string.select_team_for_battle_layer_title)

    override fun afterCreate() {
        super.afterCreate()

        content {

            addView(
                AsyncViewsWithContentListContainer<Team>(
                    context = context,
                    idGetter = Team::id,
                    invalidator = TeamsDataManager::invalidate,
                    onEmptyListInfoViewGenerator = {
                        EmptyInfoView(
                            context = context,
                            text = StringGetter(R.string.select_team_for_battle_layer_no_teams)
                        )
                    },
                    producer = TeamsDataManager.map { teamsGetter ->
                        teamsGetter.map { teams ->
                            teams.filter { team ->
                                team.id !in alreadySelectedTeamsIds
                            }
                        }
                    },
                    viewWithDataGenerator = {
                        TeamView(
                            context = context,
                            onClick = {
                                callback.addTeam(it.id)
                                managerConnector.goBack()
                            }
                        )
                    }
                ).applyLinearParams {
                    setMatchParentWidth()
                    setStretchedHeight()
                }
            )

        }

    }


}