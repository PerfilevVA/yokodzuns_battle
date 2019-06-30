package com.sibedge.yokodzun.android.layers.sections.edit

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.description.EditSectionDescriptionLayer
import com.sibedge.yokodzun.android.ui.cell.AdminBattleViewUtils
import com.sibedge.yokodzun.android.ui.plus_minus.PlusMinusColumnInfo
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.battle.Section
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter


object AdminEditSectionsLayerUtils {

    private data class Action(
        val title: StringGetter,
        val onClick: (Section, SectionsEditingCallback) -> Unit
    )

    private val ACTIONS = listOf(
        Action(StringGetter(R.string.section_action_add_subsection)) { section, callback ->
            callback.addSubsection(section.id)
        },
        Action(StringGetter(R.string.section_action_edit_description)) { section, callback ->
            AppActivityConnector.showLayer({
                EditSectionDescriptionLayer.newInstance(it, section, callback)
            })
        },
        Action(StringGetter(R.string.section_action_change_weight)) { section, callback ->
            AppActivityConnector.showPlusMinusDialog(
                title = StringGetter(R.string.admin_edit_sections_layer_section_change_weight_title),
                text = StringGetter(R.string.admin_edit_sections_layer_section_change_weight_text),
                confirmButtonText = StringGetter(R.string.dialog_save),
                initialValue = section.weight,
                valueToStringConverter = { it.toString().toGetter() },
                columns = listOf(
                    PlusMinusColumnInfo(
                        title = "10".toString().toGetter(),
                        actionMinus = { it - 10 },
                        actionPlus = { it + 10 }
                    ),
                    PlusMinusColumnInfo(
                        title = "1".toString().toGetter(),
                        actionMinus = { it - 1 },
                        actionPlus = { it + 1 }
                    )
                ),
                availableValueRange = 1..100
            ) {
                callback.updateWeight(section.id, it)
                return@showPlusMinusDialog true
            }
        },
        Action(StringGetter(R.string.section_action_remove)) { section, callback ->
            AppActivityConnector.showConfirmDialog(
                title = StringGetter(R.string.admin_edit_sections_layer_section_remove_title),
                text = StringGetter(R.string.admin_edit_sections_layer_section_remove_text),
                confirmText = StringGetter(R.string.dialog_remove)
            ) {
                callback.remove(section.id)
            }
        }
    )

    fun showSectionActions(
        section: Section,
        callback: SectionsEditingCallback
    ) = AppActivityConnector.showBottomSheet {
        title(section.entityNameWithTitle)
        ACTIONS.forEach { action ->
            closeItem(action.title) { action.onClick(section, callback) }
        }
    }

}