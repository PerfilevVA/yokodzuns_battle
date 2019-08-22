package com.sibedge.yokodzun.android.layers.rate.list

import android.content.Context
import com.sibedge.yokodzun.android.data.RaterRatesDataManager
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItem
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItemType
import com.sibedge.yokodzun.android.layers.rate.list.item.view.ParameterTitle
import com.sibedge.yokodzun.android.layers.rate.list.item.view.TeamTitle
import com.sibedge.yokodzun.android.layers.rate.list.item.view.rate.RateView
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.Team
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.ui.view.list.base.BaseList
import ru.hnau.androidutils.ui.view.list.base.BaseListOrientation
import ru.hnau.jutils.producer.Producer


class RatesList(
    context: Context,
    battle: Battle,
    sectionId: String,
    info: Producer<Triple<List<Team>, List<Parameter>, Map<RaterRatesDataManager.Key, Float>>>
) : BaseList<RatesListItem>(
    context = context,
    itemsProducer = info.map { (teams, parameters, rates) ->
        val battleParameters = parameters.filter { parameter ->
            battle.parameters.any { battleParameter ->
                battleParameter.id == parameter.id
            }
        }
        val battleTeams = teams.filter { team ->
            team.id in battle.teamsIds
        }
        battleTeams.map { team ->
            listOf(RatesListItem.createTeamTitle(team)) +
                    battleParameters.map { parameter ->
                        val rateKey = RaterRatesDataManager.Key(
                            battleId = battle.id,
                            sectionId = sectionId,
                            teamId = team.id,
                            parameterId = parameter.id
                        )
                        val rateValue = rates[rateKey]
                        listOf(
                            RatesListItem.createParameterTitle(parameter),
                            RatesListItem.createRateItem(rateKey, rateValue)
                        )
                    }.flatten()
        }.flatten()
    },
    fixedSize = false,
    orientation = BaseListOrientation.VERTICAL,
    itemTypeResolver = RatesListItem::itemTypeKey,
    viewWrappersCreator = { itemTypeKey ->
        when (itemTypeKey) {
            RatesListItemType.RATE_ITEM.key -> RateView(context)
            RatesListItemType.TEAM_TITLE.key -> TeamTitle(context)
            else -> ParameterTitle(context)
        }
    }
)