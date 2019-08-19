package com.sibedge.yokodzun.android.layers.rate

import android.content.Context
import com.sibedge.parameter.android.data.ParametersDataManager
import com.sibedge.yokodzun.android.data.RaterRatesDataManager
import com.sibedge.yokodzun.android.data.YokodzunsDataManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.rate.list.RatesList
import com.sibedge.yokodzun.android.layers.rate.list.item.RatesListItem
import com.sibedge.yokodzun.android.layers.rater.RaterLayer
import com.sibedge.yokodzun.android.ui.view.addSuspendPresenter
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.fcm.FCMMessagesReceiver
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.Rate
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.notification.type.YNotificationBattleStopped
import ru.hnau.androidutils.ui.utils.logD
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.utils.runUi
import ru.hnau.jutils.getter.SuspendGetter
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.getter.base.get
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.StateProducerSimple
import ru.hnau.jutils.producer.extensions.combine
import ru.hnau.jutils.producer.extensions.observeWhen


class RateSectionLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle,
            section: Section
        ) = RateSectionLayer(context).apply {
            this.battle = battle
            this.section = section
        }

    }

    private lateinit var battle: Battle

    @LayerState
    private lateinit var section: Section

    override val title get() = section.entityNameWithTitle

    private val rateInfoLoader = Producer.combine(
        producer1 = YokodzunsDataManager,
        producer2 = ParametersDataManager,
        producer3 = RaterRatesDataManager
    ) { yokodzuns, parameters, rates ->
        SuspendGetter.simple {
            Triple(
                yokodzuns.get(),
                parameters.get(),
                rates.get()
            )
        }
    }

    private val rateInfo = StateProducerSimple<Triple<List<Yokodzun>, List<Parameter>, List<Rate>>>()

    init {
        FCMMessagesReceiver.observeWhen(isVisibleToUserProducer) { message ->
            if (message is YNotificationBattleStopped) {
                runUi { managerConnector.showLayer(RaterLayer(context), true) }
            }
        }
    }

    override fun afterCreate() {
        super.afterCreate()

        val list by lazy {
            RatesList(
                context = context,
                battle = battle,
                sectionId = section.id,
                info = rateInfo,
                executor = uiJob
            )
        }

        content {

            addSuspendPresenter(
                producer = rateInfoLoader as Producer<GetterAsync<Unit, Triple<List<Yokodzun>, List<Parameter>, List<Rate>>>>,
                invalidator = {
                    YokodzunsDataManager.invalidate()
                    ParametersDataManager.invalidate()
                    RaterRatesDataManager.invalidate()
                },
                contentViewGenerator = {
                    rateInfo.updateState(it)
                    val listItems = rateInfo.map { (yokodzuns, parameters, rates) ->
                        val sectionRates = rates.filter { rate ->
                            (rate.battleId == battle.id).and(rate.sectionId == section.id)
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
                    }
                    listItems.map { listItemsList -> logD(listItemsList.toString()) }
                    list
                }
            ) {
                applyLinearParams {
                    setStretchedHeight()
                    setMatchParentWidth()
                }
            }

        }
    }
}