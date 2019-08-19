package com.sibedge.yokodzun.android.layers.rate.list

import android.content.Context
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItem
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItemType
import com.sibedge.yokodzun.android.layers.rate.list.item.view.ParameterTitle
import com.sibedge.yokodzun.android.layers.rate.list.item.view.YokodzunTitle
import com.sibedge.yokodzun.android.layers.rate.list.item.view.rate.RateView
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.Rate
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.ui.view.list.base.BaseList
import ru.hnau.androidutils.ui.view.list.base.BaseListOrientation
import ru.hnau.jutils.coroutines.executor.InterruptableExecutor
import ru.hnau.jutils.producer.Producer


class RatesList(
    context: Context,
    battle: Battle,
    sectionId: String,
    info: Producer<Triple<List<Yokodzun>, List<Parameter>, List<Rate>>>,
    executor: InterruptableExecutor
) : BaseList<RatesListItem>(
    context = context,
    itemsProducer = info.map { (yokodzuns, parameters, rates) ->
        val sectionRates = rates.filter { rate ->
            (rate.battleId == battle.id).and(rate.sectionId == sectionId)
        }
        val battleParameters = parameters.filter { parameter ->
            battle.parameters.any { battleParameter ->
                battleParameter.id == parameter.id
            }
        }
        val battleYokodzuns = yokodzuns.filter { yokodzuns ->
            yokodzuns.id in battle.yokodzunsIds
        }
        battleYokodzuns.map { yokodzun ->
            listOf(RatesListItem.createYokodzunTitle(yokodzun)) +
                    battleParameters.map { parameter ->
                        listOf(
                            RatesListItem.createParameterTitle(parameter),
                            RatesListItem.createRateItem(sectionRates.find { rate ->
                                (rate.yokodzunId == yokodzun.id).and(rate.parameterId == parameter.id)
                            })
                        )
                    }.flatten()
        }.flatten()
    },
    fixedSize = false,
    orientation = BaseListOrientation.VERTICAL,
    itemTypeResolver = RatesListItem::itemTypeKey,
    viewWrappersCreator = { itewmTypeKey ->
        when (itewmTypeKey) {
            RatesListItemType.RATE_ITEM.key -> RateView(context, executor)
            RatesListItemType.YOKODZUN_TITLE.key -> YokodzunTitle(context)
            else -> ParameterTitle(context)
        }
    }
)