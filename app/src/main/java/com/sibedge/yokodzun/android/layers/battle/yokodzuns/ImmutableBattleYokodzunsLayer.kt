package com.sibedge.yokodzun.android.layers.battle.yokodzuns

import android.content.Context
import com.sibedge.yokodzun.android.data.YokodzunsDataManager
import com.sibedge.yokodzun.android.layers.battle.yokodzuns.edit.BattleYokodzunsEditor
import com.sibedge.yokodzun.android.layers.battle.yokodzuns.edit.EditBattleYokodzunsLayer
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.getter.base.map
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine


class ImmutableBattleYokodzunsLayer(
    context: Context
) : BattleYokodzunsLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = ImmutableBattleYokodzunsLayer(context).apply {
            this.battle = battle
        }

    }

    @LayerState
    override lateinit var battle: Battle

    override val yokodzunsProducer =
        YokodzunsDataManager.map { yokodzunsGetter ->
        yokodzunsGetter.map { yokodzuns ->
            yokodzuns.filter { yokodzun ->
                yokodzun.id in battle.yokodzunsIds
            }
        }
    }

    override fun invalidateYokodzuns() = YokodzunsDataManager.invalidate()

}