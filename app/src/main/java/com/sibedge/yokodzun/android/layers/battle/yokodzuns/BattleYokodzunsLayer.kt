package com.sibedge.yokodzun.android.layers.battle.yokodzuns

import android.content.Context
import android.view.ViewGroup
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.ui.view.cell.YokodzunView
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncViewsWithContentListContainer
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer


abstract class BattleYokodzunsLayer(
    context: Context
) : AppLayer(
    context = context
) {

    protected abstract val battle: Battle

    override val title
        get() = StringGetter(R.string.battle_teams_layer_title, battle.description.title)

    protected abstract val yokodzunsProducer: Producer<GetterAsync<Unit, List<Yokodzun>>>

    protected open val onEmptyListInfoView by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.battle_teams_layer_no_teams_title)
        )
    }

    protected abstract fun invalidateYokodzuns()

    protected open val additionalButtonInfo: (Yokodzun) -> AdditionalButton.Info? = { null }

    protected open fun ViewGroup.configureView(listView: AsyncViewsWithContentListContainer<Yokodzun>) {}

    override fun afterCreate() {
        super.afterCreate()

        val listView = AsyncViewsWithContentListContainer(
            context = context,
            producer = yokodzunsProducer,
            onEmptyListInfoViewGenerator = { onEmptyListInfoView },
            invalidator = this::invalidateYokodzuns,
            idGetter = Yokodzun::id,
            viewWithDataGenerator = {
                YokodzunView(
                    context = context,
                    onClick = {},
                    additionalButtonInfo = additionalButtonInfo
                )
            }
        )

        content {
            addFrameLayout {

                applyLinearParams {
                    setStretchedHeight()
                    setMatchParentWidth()
                }

                addView(listView)
                configureView(listView)

            }

        }
    }


}