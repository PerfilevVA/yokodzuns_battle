package com.sibedge.yokodzun.android.layers.admin.pages.teams

import android.content.Context
import android.widget.FrameLayout
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.TeamsDataManager
import com.sibedge.yokodzun.android.ui.view.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.view.cell.TeamView
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncViewsWithContentListContainer
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.Team
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.not


class AdminTeamsPageView(
    context: Context,
    private val coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
) : FrameLayout(
    context
) {

    init {
        val list =
            AsyncViewsWithContentListContainer<Team>(
                context = context,
                idGetter = Team::id,
                invalidator = TeamsDataManager::invalidate,
                onEmptyListInfoViewGenerator = {
                    EmptyInfoView(
                        context = context,
                        text = StringGetter(R.string.admin_layer_teams_page_no_teams_title),
                        button = StringGetter(R.string.admin_layer_teams_page_add_team) to this::onAddTeamClick
                    )
                },
                producer = TeamsDataManager as Producer<GetterAsync<Unit, List<Team>>>,
                viewWithDataGenerator = {
                    TeamView(
                        context = context,
                        onClick = { AdminTeamUtils.showTeamActions(it, coroutinesExecutor) }
                    )
                }
            )

        addChild(list)

        addPrimaryActionButton(
            icon = DrawableGetter(R.drawable.ic_add_fg),
            title = StringGetter(R.string.admin_layer_teams_page_add_team),
            needShowTitle = list.onListScrolledToTopProducer.not(),
            onClick = this::onAddTeamClick
        ) {
            applyFrameParams {
                setMargins(SizeManager.DEFAULT_SEPARATION)
                setEndBottomGravity()
            }
        }
    }

    private fun onAddTeamClick() =
        coroutinesExecutor { TeamsDataManager.createNew() }

}