package com.sibedge.parameter.android.layers.select

import android.content.Context
import com.sibedge.parameter.android.data.ParametersDataManager
import com.sibedge.parameter.android.ui.view.cell.ParameterView
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.battle.parameters.edit.EditBattleParametersCallback
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncViewsWithContentListContainer
import com.sibedge.yokodzun.common.data.Parameter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.base.map


class SelectParameterForBattleLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            alreadySelectedParametersIds: List<String>,
            callback: EditBattleParametersCallback
        ) = SelectParameterForBattleLayer(context).apply {
            this.callback = callback
            this.alreadySelectedParametersIds = alreadySelectedParametersIds
        }

    }

    @LayerState
    private lateinit var callback: EditBattleParametersCallback

    @LayerState
    private lateinit var alreadySelectedParametersIds: List<String>

    override val title = StringGetter(R.string.select_parameter_for_battle_layer_title)

    override fun afterCreate() {
        super.afterCreate()

        content {

            addView(
                AsyncViewsWithContentListContainer<Parameter>(
                    context = context,
                    idGetter = Parameter::id,
                    invalidator = ParametersDataManager::invalidate,
                    onEmptyListInfoViewGenerator = {
                        EmptyInfoView(
                            context = context,
                            text = StringGetter(R.string.select_parameter_for_battle_layer_no_parameters)
                        )
                    },
                    producer = ParametersDataManager.map { parametersGetter ->
                        parametersGetter.map { parameters ->
                            parameters.filter { parameter ->
                                parameter.id !in alreadySelectedParametersIds
                            }
                        }
                    },
                    viewWithDataGenerator = {
                        ParameterView(
                            context = context,
                            onClick = {
                                callback.addParameter(it.id)
                                managerConnector.goBack()
                            }
                        )
                    }
                ).applyLinearParams {
                    setMatchParentWidth()
                    setStretchedHeight()
                }
            )

        }

    }


}