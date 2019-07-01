package com.sibedge.parameter.android.layers.battle.parameters

import android.content.Context
import android.view.ViewGroup
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.battle.parameters.item.BattleFullParameter
import com.sibedge.yokodzun.android.layers.battle.parameters.item.BattleFullParameterView
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncItemsListContaner
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer


abstract class BattleParametersLayer(
    context: Context
) : AppLayer(
    context = context
) {

    protected abstract val battle: Battle

    override val title
        get() = StringGetter(R.string.battle_parameters_layer_title, battle.description.title)

    protected abstract val parametersProducer: Producer<GetterAsync<Unit, List<BattleFullParameter>>>

    protected open val onEmptyListInfoView by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.battle_parameters_layer_no_parameters_title)
        )
    }

    protected abstract fun invalidateBattleFullParameters()

    protected open val additionalButtonInfo: (BattleFullParameter) -> AdditionalButton.Info? =
        { null }

    protected open fun ViewGroup.configureView(listView: AsyncItemsListContaner<BattleFullParameter>) {}

    protected open fun onClick(parameter: BattleFullParameter) {}

    override fun afterCreate() {
        super.afterCreate()

        val listView = AsyncItemsListContaner(
            context = context,
            producer = parametersProducer,
            onEmptyListInfoViewGenerator = { onEmptyListInfoView },
            invalidator = this::invalidateBattleFullParameters,
            idGetter = BattleFullParameter::id,
            viewWrappersCreator = {
                BattleFullParameterView(
                    context = context,
                    onClick = this@BattleParametersLayer::onClick
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