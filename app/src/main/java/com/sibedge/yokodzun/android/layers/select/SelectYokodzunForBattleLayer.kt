package com.sibedge.yokodzun.android.layers.select

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.YokodzunsDataManager
import com.sibedge.yokodzun.android.layers.admin.pages.yokodzuns.AdminYokodzunUtils
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.battle.yokodzuns.edit.EditBattleYokodzunsCallback
import com.sibedge.yokodzun.android.ui.view.cell.YokodzunView
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncViewsWithContentListContainer
import com.sibedge.yokodzun.common.data.Yokodzun
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.getter.base.map
import ru.hnau.jutils.producer.Producer


class SelectYokodzunForBattleLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            alreadySelectedYokodzunsIds: List<String>,
            callback: EditBattleYokodzunsCallback
        ) = SelectYokodzunForBattleLayer(context).apply {
            this.callback = callback
            this.alreadySelectedYokodzunsIds = alreadySelectedYokodzunsIds
        }

    }

    @LayerState
    private lateinit var callback: EditBattleYokodzunsCallback

    @LayerState
    private lateinit var alreadySelectedYokodzunsIds: List<String>

    override val title = StringGetter(R.string.select_team_for_battle_layer_title)

    override fun afterCreate() {
        super.afterCreate()

        content {

            addView(
                AsyncViewsWithContentListContainer<Yokodzun>(
                    context = context,
                    idGetter = Yokodzun::id,
                    invalidator = YokodzunsDataManager::invalidate,
                    onEmptyListInfoViewGenerator = {
                        EmptyInfoView(
                            context = context,
                            text = StringGetter(R.string.select_team_for_battle_layer_no_teams)
                        )
                    },
                    producer = YokodzunsDataManager.map { yokodzunsGetter ->
                        yokodzunsGetter.map { yokodzuns ->
                            yokodzuns.filter { yokodzun ->
                                yokodzun.id !in alreadySelectedYokodzunsIds
                            }
                        }
                    },
                    viewWithDataGenerator = {
                        YokodzunView(
                            context = context,
                            onClick = {
                                callback.addYokodzun(it.id)
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