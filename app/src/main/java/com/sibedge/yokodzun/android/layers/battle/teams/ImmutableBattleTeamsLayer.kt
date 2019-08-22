package com.sibedge.yokodzun.android.layers.battle.teams

import android.content.Context
import com.sibedge.yokodzun.android.data.TeamsDataManager
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.jutils.getter.base.map


class ImmutableBattleTeamsLayer(
    context: Context
) : BattleTeamsLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = ImmutableBattleTeamsLayer(context).apply {
            this.battle = battle
        }

    }

    @LayerState
    override lateinit var battle: Battle

    override val teamsProducer =
        TeamsDataManager.map { yokodzunsGetter ->
        yokodzunsGetter.map { yokodzuns ->
            yokodzuns.filter { yokodzun ->
                yokodzun.id in battle.teamsIds
            }
        }
    }

    override fun invalidateTeams() = TeamsDataManager.invalidate()

}