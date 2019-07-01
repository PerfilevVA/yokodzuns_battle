package com.sibedge.yokodzun.android.layers.battle.yokodzuns.edit

import android.content.Context
import android.view.ViewGroup
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.BattlesDataManager
import com.sibedge.yokodzun.android.layers.battle.yokodzuns.BattleYokodzunsLayer
import com.sibedge.yokodzun.android.ui.view.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncViewsWithContentListContainer
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.Yokodzun
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.jutils.producer.extensions.not


class EditBattleYokodzunsLayer(
    context: Context
) : BattleYokodzunsLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = EditBattleYokodzunsLayer(context).apply {
            this.battle = battle
            this.editor = BattleYokodzunsEditor(battle)
        }

    }

    @LayerState
    override lateinit var battle: Battle

    @LayerState
    private lateinit var editor: BattleYokodzunsEditor

    override val yokodzunsProducer
        get() = editor.selectedYokodzuns

    override fun invalidateYokodzuns() = editor.invalidate()

    override val onEmptyListInfoView by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.battle_yokodzuns_layer_no_yokodzuns_title),
            button = StringGetter(R.string.battle_yokodzuns_layer_no_yokodzuns_add_yokodzun) to editor::selectNewYokodzun
        )
    }

    override val additionalButtonInfo by lazy {
        { yokodzun: Yokodzun -> editor.createAdditionalButtonInfo(yokodzun) }
    }

    override fun ViewGroup.configureView(listView: AsyncViewsWithContentListContainer<Yokodzun>) {
        addPrimaryActionButton(
            icon = DrawableGetter(R.drawable.ic_add_fg),
            title = StringGetter(R.string.battle_yokodzuns_layer_no_yokodzuns_add_yokodzun),
            needShowTitle = listView.onListScrolledToTopProducer.not(),
            onClick = editor::selectNewYokodzun
        ) {
            applyFrameParams {
                setMargins(SizeManager.DEFAULT_SEPARATION)
                setEndBottomGravity()
            }
        }
    }

    override fun handleGoBack(): Boolean {
        uiJobLocked {
            BattlesDataManager.updateYokodzunsIds(
                battleId = battle.id,
                yokodzunsIds = editor.selectedYokodzunsIds
            )
            managerConnector.goBack()
        }
        return true
    }

}