package com.sibedge.yokodzun.android.layers.rate.list

import android.content.Context
import com.sibedge.yokodzun.android.data.RaterRatesDataManager
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItem
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItemType
import com.sibedge.yokodzun.android.layers.rate.list.item.view.ParameterTitle
import com.sibedge.yokodzun.android.layers.rate.list.item.view.YokodzunTitle
import com.sibedge.yokodzun.android.layers.rate.list.item.view.rate.RateView
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.ui.view.list.base.BaseList
import ru.hnau.androidutils.ui.view.list.base.BaseListOrientation
import ru.hnau.jutils.coroutines.executor.InterruptableExecutor
import ru.hnau.jutils.producer.Producer


class RatesList(
    context: Context,
    sectionId: String,
    info: Producer<Triple<Battle, List<Parameter>, List<Yokodzun>>>,
    executor: InterruptableExecutor
) : BaseList<RatesListItem>(
    context = context,
    itemsProducer = info.map { (battle, parameters, yokodzuns) ->
        val battleParameters = parameters.filter { parameter ->
            battle.parameters.any { battleParameter ->
                battleParameter.id == parameter.id
            }
        }
        val battleYokodzuns = yokodzuns.filter { yokodzsun ->
            yokodzsun.id in battle.yokodzunsIds
        }
        battleYokodzuns.map { yokodzun ->
            listOf(RatesListItem.createYokodzunTitle(yokodzun)) +
                    battleParameters.map { parameter ->
                        listOf(
                            RatesListItem.createParameterTitle(parameter),
                            RatesListItem.createRateItem(
                                RaterRatesDataManager.Key(
                                    battleId = battle.id,
                                    parameterId = parameter.id,
                                    sectionId = sectionId,
                                    yokodzunId = yokodzun.id
                                )
                            )
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