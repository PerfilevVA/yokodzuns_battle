package com.sibedge.yokodzun.android.layers.admin.pages.teams

import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.TeamsDataManager
import com.sibedge.yokodzun.android.layers.description.EditTeamDescriptionLayer
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.common.data.Team
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.StringGetter


object AdminTeamUtils {

    fun showTeamActions(
        team: Team,
        coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
    ) = AppActivityConnector.showBottomSheet {
        title(team.entityNameWithTitle)
        closeItem(StringGetter(R.string.team_action_edit_description)) {
            AppActivityConnector.showLayer({
                EditTeamDescriptionLayer.newInstance(context, team)
            })
        }
        closeItem(StringGetter(R.string.team_action_remove)) {
            AppActivityConnector.showConfirmDialog(
                title = StringGetter(R.string.team_action_remove_confirm_dialog_title),
                text = StringGetter(R.string.team_action_remove_confirm_dialog_text),
                confirmText = StringGetter(R.string.dialog_remove)
            ) {
                coroutinesExecutor { TeamsDataManager.remove(teamId = team.id) }
            }
        }
    }

}