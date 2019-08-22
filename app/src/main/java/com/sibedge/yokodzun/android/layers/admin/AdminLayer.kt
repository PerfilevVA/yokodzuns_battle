package com.sibedge.yokodzun.android.layers.admin

import android.content.Context
import com.sibedge.parameter.android.layers.admin.pages.parameter.AdminParametersPageView
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.admin.pages.battles.AdminBattlesPageView
import com.sibedge.yokodzun.android.layers.admin.pages.teams.AdminTeamsPageView
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.view.pager.Pager
import com.sibedge.yokodzun.android.ui.view.pager.PagerPage
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.helpers.VariableConnector


class AdminLayer(
    context: Context
) : AppLayer(
    context = context,
    showGoBackButton = false
) {

    override val title = StringGetter(R.string.admin_layer_title)

    @LayerState
    private var pageNumber = 0

    override fun afterCreate() {
        super.afterCreate()

        val pager = Pager(
            context = context,
            pages = listOf(
                PagerPage(
                    title = StringGetter(R.string.admin_layer_page_title_battles),
                    viewCreator = { AdminBattlesPageView(context, this::uiJobLocked) }
                ),
                PagerPage(
                    title = StringGetter(R.string.admin_layer_page_title_teams),
                    viewCreator = { AdminTeamsPageView(context, this::uiJobLocked) }
                ),
                PagerPage(
                    title = StringGetter(R.string.admin_layer_page_title_parameters),
                    viewCreator = { AdminParametersPageView(context, this::uiJobLocked) }
                )
            ),
            selectedPage = VariableConnector(
                getter = { pageNumber },
                setter = { pageNumber = it }
            )
        )

        topContent {
            addView(pager.tabLayout)
        }

        content {
            addView(pager.pager.applyLinearParams {
                setMatchParentWidth()
                setStretchedHeight()
            })
        }

    }

}