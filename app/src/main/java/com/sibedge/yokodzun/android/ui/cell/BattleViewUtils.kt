package com.sibedge.yokodzun.android.ui.cell

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding


object BattleViewUtils {

    private fun createCountView(
        context: Context,
        title: StringGetter,
        textColor: ColorGetter,
        fromColor: ColorGetter,
        toColor: ColorGetter,
        toCount: Battle.() -> Int?,
        onClick: (Battle) -> Unit
    ) = object : ViewWithContent<Battle> {

        override val view = CountView(
            context = context,
            title = title,
            textColor = textColor,
            fromColor = fromColor,
            toColor = toColor,
            onClick = this::onClick
        ).apply {
            applyPadding(SizeManager.SMALL_SEPARATION)
        }

        override var content: Battle? = null
            set(value) {
                field = value
                view.content = value?.toCount()
            }

        private fun onClick() {
            content?.let(onClick)
        }

    }


    fun createYokodzunsCountView(
        context: Context,
        onClick: (Battle) -> Unit
    ) = createCountView(
        context = context,
        title = StringGetter(R.string.battle_view_yokodzuns),
        fromColor = ColorManager.ORANGE_LIGHT,
        toColor = ColorManager.ORANGE_DARK,
        textColor = ColorManager.ORANGE,
        toCount = { yokodzunsIds.size },
        onClick = onClick
    )

    fun createParametersCountView(
        context: Context,
        onClick: (Battle) -> Unit
    ) = createCountView(
        context = context,
        title = StringGetter(R.string.battle_view_parameters),
        fromColor = ColorManager.GREEN_LIGHT,
        toColor = ColorManager.GREEN_DARK,
        textColor = ColorManager.GREEN,
        toCount = { parameters.size },
        onClick = onClick
    )

    fun createSectionsCountView(
        context: Context,
        onClick: (Battle) -> Unit
    ) = createCountView(
        context = context,
        title = StringGetter(R.string.battle_view_sections),
        fromColor = ColorManager.PRIMARY_LIGHT,
        toColor = ColorManager.PRIMARY_DARK,
        textColor = ColorManager.PRIMARY,
        toCount = { sections.size },
        onClick = onClick
    )

}