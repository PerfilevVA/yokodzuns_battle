package com.sibedge.yokodzun.android.utils

import com.sibedge.yokodzun.common.data.Rate
import com.sibedge.yokodzun.common.data.Team
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.jutils.takeIfPositive


object RateCalculator {

    data class Value(
        val team: Team,
        val value: Float? = null,
        val marksCount: Int = 0
    )

    fun calcRate(
        battle: Battle,
        teams: List<Team>,
        battleRates: List<Rate>,
        parameterId: String? = null
    ): List<Value> {

        val parametersWeights = battle.parameters.associate { (id, weight) ->
            id to weight
        }

        val sectionsWeights = battle.sections.associate { section ->
            section.id to section.weight
        }

        val rates = parameterId?.let { id ->
            battleRates.filter { it.parameterId == id }
        } ?: battleRates

        val teamsRates = rates.groupBy { it.teamId }

        return teams
            .filter { it.id in battle.teamsIds }
            .map { team ->

                val teamId = team.id

                var weight = 0
                var sum = 0f
                val teamRates = teamsRates[teamId] ?: emptyList()
                (teamRates).forEach { rate ->

                    val paramWeight = parametersWeights[rate.parameterId]
                        ?.takeIfPositive() ?: return@forEach

                    val sectionWeight = sectionsWeights[rate.sectionId]
                        ?.takeIfPositive() ?: return@forEach

                    val rateWeight = paramWeight * sectionWeight

                    weight += rateWeight
                    sum += rate.value * rateWeight
                }

                weight.takeIfPositive()?.let { positiveWeight ->
                    Value(
                        team = team,
                        value = sum / positiveWeight.toFloat(),
                        marksCount = teamRates.size
                    )
                } ?: Value(
                    team = team
                )
            }
            .sortedByDescending { value ->
                value.value ?: 0f
            }

    }

}