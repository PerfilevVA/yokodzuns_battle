package com.sibedge.yokodzun.android.utils

import com.sibedge.yokodzun.common.data.Rate
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.jutils.handle
import ru.hnau.jutils.takeIfPositive


object RateCalculator {

    data class Value(
        val yokodzun: Yokodzun,
        val value: Float? = null,
        val marksCount: Int = 0
    )

    fun calcRate(
        battle: Battle,
        yokodzuns: List<Yokodzun>,
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

        val yokodzunsRates = rates.groupBy { it.yokodzunId }

        return yokodzuns
            .filter { it.id in battle.yokodzunsIds }
            .map { yokodzun ->

                val yokodzunId = yokodzun.id

                var weight = 0
                var sum = 0f
                val yokodzunRates = yokodzunsRates[yokodzunId] ?: emptyList()
                (yokodzunRates).forEach { rate ->

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
                        yokodzun = yokodzun,
                        value = sum / positiveWeight.toFloat(),
                        marksCount = yokodzunRates.size
                    )
                } ?: Value(
                    yokodzun = yokodzun
                )
            }
            .sortedByDescending { value ->
                value.value ?: 0f
            }

    }

}