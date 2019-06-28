package com.sibedge.yokodzun.android.ui.cell

import android.content.Context
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.button.YButton
import com.sibedge.yokodzun.android.ui.button.YButtonInfo
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.dp32
import ru.hnau.androidutils.ui.view.utils.apply.applyEndGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyTopPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams


class AdminBattleAdditionalView(
    context: Context
) : LinearLayout(
    context
), ViewWithContent<Battle> {

    override val view = this

    override var content: Battle? = null
        set(value) {
            field = value
            updateContent(value)
        }

    private val buttonRemove by lazy {
        createButton(
            title = StringGetter(R.string.battle_view_admin_action_remove),
            onClick = this::remove,
            colorTo = ColorManager.RED_DARK,
            colorFrom = ColorManager.RED_LIGHT
        )
    }

    private val buttonEdit by lazy {
        createButton(
            title = StringGetter(R.string.battle_view_admin_action_edit),
            onClick = this::edit,
            colorTo = ColorManager.PRIMARY_DARK,
            colorFrom = ColorManager.PRIMARY_LIGHT
        )
    }

    private val buttonStart by lazy {
        createButton(
            title = StringGetter(R.string.battle_view_admin_action_start),
            onClick = this::start,
            colorTo = ColorManager.GREEN_DARK,
            colorFrom = ColorManager.GREEN_LIGHT
        )
    }

    private val buttonStop by lazy {
        createButton(
            title = StringGetter(R.string.battle_view_admin_action_stop),
            onClick = this::stop,
            colorTo = ColorManager.ORANGE_DARK,
            colorFrom = ColorManager.ORANGE_LIGHT
        )
    }

    init {
        applyHorizontalOrientation()
        applyEndGravity()
        applyTopPadding(SizeManager.DEFAULT_SEPARATION)
        applyHorizontalPadding(SizeManager.SMALL_SEPARATION + SizeManager.EXTRA_SMALL_SEPARATION)
    }

    private fun createButton(
        title: StringGetter,
        colorFrom: ColorGetter,
        colorTo: ColorGetter,
        onClick: () -> Unit
    ) = YButton(
        context = context,
        onClick = onClick,
        text = title,
        info = YButtonInfo(
            textSize = SizeManager.TEXT_12,
            height = dp32,
            font = FontManager.UBUNTU_BOLD,
            textColor = ColorManager.FG,
            backgroundColorFrom = colorFrom,
            backgroundColorTo = colorTo
        )
    ).applyLinearParams {
        setHorizontalMargins(SizeManager.EXTRA_SMALL_SEPARATION)
    }

    private fun updateContent(battle: Battle?) {
        removeAllViews()
        battle ?: return
        if (battle.status == BattleStatus.IN_PROGRESS) {
            addView(buttonStop)
        } else {
            addView(buttonRemove)
            addView(buttonEdit)
            addView(buttonStart)
        }
    }

    private fun edit() {

    }

    private fun remove() {

    }

    private fun start() {

    }

    private fun stop() {

    }

}