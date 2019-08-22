package com.sibedge.yokodzun.android.ui.view.cell.battle

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.ui.view.ClickableCountView
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding


object BattleViewUtils {

    private fun createCountView(
        context: Context,
        title: StringGetter,
        color: ColorTriple,
        toCount: Battle.() -> Int?,
        onClick: (Battle) -> Unit
    ) = object : ViewWithData<Battle> {

        override val view = ClickableCountView(
            context = context,
            title = title,
            color = color,
            textSize = SizeManager.TEXT_12,
            titleCountSeparation = SizeManager.EXTRA_SMALL_SEPARATION,
            onClick = this::onClick
        ).applyPadding(SizeManager.SMALL_SEPARATION)

        override var data: Battle? = null
            set(value) {
                field = value
                view.data = value?.toCount()
            }

        private fun onClick() {
            data?.let(onClick)
        }

    }


    fun createTeamsCountView(
        context: Context,
        onClick: (Battle) -> Unit
    ) = createCountView(
        context = context,
        title = StringGetter(R.string.battle_view_teams),
        color = ColorManager.ORANGE_TRIPLE,
        toCount = { teamsIds.size },
        onClick = onClick
    )

    fun createParametersCountView(
        context: Context,
        onClick: (Battle) -> Unit
    ) = createCountView(
        context = context,
        title = StringGetter(R.string.battle_view_parameters),
        color = ColorManager.GREEN_TRIPLE,
        toCount = { parameters.size },
        onClick = onClick
    )

    fun createSectionsCountView(
        context: Context,
        onClick: (Battle) -> Unit
    ) = createCountView(
        context = context,
        title = StringGetter(R.string.battle_view_sections),
        color = ColorManager.PRIMARY_TRIPLE,
        toCount = { sections.size },
        onClick = onClick
    )

}