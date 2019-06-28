package com.sibedge.yokodzun.android.ui.cell

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.admin.AdminAllBattlesDataManager
import com.sibedge.yokodzun.android.layers.EditDescriptionLayer
import com.sibedge.yokodzun.android.layers.SectionsLayer
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.jutils.handle


object AdminBattleViewUtils {

    private data class Action(
        val title: StringGetter,
        val onClick: () -> Unit
    )

    fun onClick(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) {
        AppActivityConnector.showBottomSheet {
            title(battle.entityNameWithTitle)
            val actions = (battle.status == BattleStatus.IN_PROGRESS).handle(
                onTrue = { getStartedActions(battle, coroutinesExecutor) },
                onFalse = { getStoppedActions(battle, coroutinesExecutor) }
            )
            actions.forEach { (title, onClick) ->
                closeItem(title, onClick)
            }
        }
    }

    private fun getStartedActions(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = listOf(
        Action(StringGetter(R.string.battle_view_admin_action_stop)) {
            AppActivityConnector.showConfirmDialog(
                title = StringGetter(R.string.battle_view_admin_stop_confirm_dialog_title),
                text = StringGetter(R.string.battle_view_admin_stop_confirm_dialog_text),
                confirmText = StringGetter(R.string.battle_view_admin_stop_confirm_dialog_button)
            ) {
                coroutinesExecutor { AdminAllBattlesDataManager.stop(battleId = battle.id) }
            }
        }
    )

    private fun getStoppedActions(
        battle: Battle,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = listOf(
        Action(StringGetter(R.string.battle_view_admin_action_start)) {
            AppActivityConnector.showConfirmDialog(
                title = StringGetter(R.string.battle_view_admin_start_confirm_dialog_title),
                text = StringGetter(R.string.battle_view_admin_start_confirm_dialog_text),
                confirmText = StringGetter(R.string.battle_view_admin_start_confirm_dialog_button)
            ) {
                coroutinesExecutor { AdminAllBattlesDataManager.start(battleId = battle.id) }
            }
        },
        Action(StringGetter(R.string.battle_view_admin_action_edit)) {
            AppActivityConnector.showLayer({ context ->
                EditDescriptionLayer.newInstance(
                    context = context,
                    title = StringGetter(R.string.battle_view_admin_edit_description_title),
                    titleHint = StringGetter(R.string.battle_view_admin_edit_description_title_hint),
                    logoUrlHint = StringGetter(R.string.battle_view_admin_edit_description_logo_url_hint),
                    descriptionHint = StringGetter(R.string.battle_view_admin_edit_description_description_hint),
                    editingDescription = battle.description,
                    save = { coroutinesExecutor, newDescription ->
                        coroutinesExecutor {
                            AdminAllBattlesDataManager.updateDescription(battle.id, newDescription)
                            AppActivityConnector.goBack()
                        }

                    }
                )
            })
        },
        Action(StringGetter(R.string.battle_view_admin_action_update_raters)) {
            //TODO
        },
        Action(StringGetter(R.string.battle_view_admin_action_update_yokodzuns)) {
            editBattleYokodzuns(battle)
        },
        Action(StringGetter(R.string.battle_view_admin_action_update_parameters)) {
            editBattleParameters(battle)
        },
        Action(StringGetter(R.string.battle_view_admin_action_update_sections)) {
            editBattleSectios(battle)
        },
        Action(StringGetter(R.string.battle_view_admin_action_remove)) {
            AppActivityConnector.showConfirmDialog(
                title = StringGetter(R.string.battle_view_admin_remove_confirm_dialog_title),
                text = StringGetter(R.string.battle_view_admin_remove_confirm_dialog_text),
                confirmText = StringGetter(R.string.dialog_remove)
            ) {
                coroutinesExecutor { AdminAllBattlesDataManager.remove(battleId = battle.id) }
            }
        }
    )

    fun editBattleYokodzuns(battle: Battle) {
        //TODO
    }

    fun editBattleParameters(battle: Battle) {
        //TODO
    }

    fun editBattleSectios(battle: Battle) {
        AppActivityConnector.showLayer({ context -> SectionsLayer.newInstance(context, battle) })
    }

}