package com.sibedge.yokodzun.android.layers.rate

import android.content.Context
import com.sibedge.parameter.android.data.ParametersDataManager
import com.sibedge.yokodzun.android.data.RaterBattleDataManager
import com.sibedge.yokodzun.android.data.YokodzunsDataManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.login.LoginLayer
import com.sibedge.yokodzun.android.layers.rate.list.RatesList
import com.sibedge.yokodzun.android.layers.rater.RaterLayer
import com.sibedge.yokodzun.android.ui.view.addSuspendPresenter
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.fcm.FCMMessagesReceiver
import com.sibedge.yokodzun.common.data.Parameter
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.notification.type.YNotificationBattleStopped
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
            section: Section
        ) = RateSectionLayer(context).apply {
            this.section = section
        }

    }

    @LayerState
    private lateinit var section: Section

    override val title get() = section.entityNameWithTitle

    private val rateInfoLoader = Producer.combine(
        producer1 = RaterBattleDataManager,
        producer2 = ParametersDataManager,
        producer3 = YokodzunsDataManager
    ) { battle, parameters, yokodzuns ->
        SuspendGetter.simple {
            Triple(
                battle.get(),
                parameters.get(),
                yokodzuns.get()
            )
        }
    }

    private val rateInfo = StateProducerSimple<Triple<Battle, List<Parameter>, List<Yokodzun>>>()

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
                sectionId = section.id,
                info = rateInfo,
                executor = uiJob
            )
        }

        content {

            addSuspendPresenter(
                producer = rateInfoLoader as Producer<GetterAsync<Unit, Triple<Battle, List<Parameter>, List<Yokodzun>>>>,
                invalidator = {
                    RaterBattleDataManager.invalidate()
                    ParametersDataManager.invalidate()
                    YokodzunsDataManager.invalidate()
                },
                contentViewGenerator = {
                    rateInfo.updateState(it)
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