package com.sibedge.parameter.android.layers.battle.parameters.edit

import android.content.Context
import android.view.ViewGroup
import com.sibedge.parameter.android.layers.battle.parameters.BattleParametersLayer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.AdminBattlesDataManager
import com.sibedge.yokodzun.android.layers.battle.parameters.item.BattleFullParameter
import com.sibedge.yokodzun.android.ui.view.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncItemsListContaner
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.jutils.producer.extensions.not


class EditBattleParametersLayer(
    context: Context
) : BattleParametersLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = EditBattleParametersLayer(context).apply {
            this.battle = battle
            this.editor = BattleParametersEditor(battle)
        }

    }

    @LayerState
    override lateinit var battle: Battle

    @LayerState
    private lateinit var editor: BattleParametersEditor

    override val parametersProducer
        get() = editor.selectedParameters

    override fun invalidateBattleFullParameters() = editor.invalidate()

    override val onEmptyListInfoView by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.battle_parameters_layer_no_parameters_title),
            button = StringGetter(R.string.battle_parameters_layer_no_parameters_add_parameter) to editor::selectNewParameter
        )
    }

    override fun onClick(parameter: BattleFullParameter) =
        editor.onParameterCick(parameter)

    override fun ViewGroup.configureView(listView: AsyncItemsListContaner<BattleFullParameter>) {
        addPrimaryActionButton(
            icon = DrawableGetter(R.drawable.ic_add_fg),
            title = StringGetter(R.string.battle_parameters_layer_no_parameters_add_parameter),
            needShowTitle = listView.onListScrolledToTopProducer.not(),
            onClick = editor::selectNewParameter
        ) {
            applyFrameParams {
                setMargins(SizeManager.DEFAULT_SEPARATION)
                setEndBottomGravity()
            }
        }
    }

    override fun handleGoBack(): Boolean {
        uiJobLocked {
            AdminBattlesDataManager.updateParameters(
                battleId = battle.id,
                parameters = editor.alreadySelectedParameters
            )
            managerConnector.goBack()
        }
        return true
    }

}