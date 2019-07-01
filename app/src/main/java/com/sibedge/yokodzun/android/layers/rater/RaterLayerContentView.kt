package com.sibedge.yokodzun.android.layers.rater

import android.content.Context
import android.graphics.fonts.Font
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import com.sibedge.parameter.android.layers.battle.parameters.ImmutableBattleParametersLayer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.battle.yokodzuns.ImmutableBattleYokodzunsLayer
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addBottomButtonView
import com.sibedge.yokodzun.android.ui.hierarchy_utils.addButtonView
import com.sibedge.yokodzun.android.ui.view.AsyncImageView
import com.sibedge.yokodzun.android.ui.view.ClickableCountView
import com.sibedge.yokodzun.android.ui.view.button.YButtonInfo
import com.sibedge.yokodzun.android.ui.view.cell.battle.BattleViewUtils
import com.sibedge.yokodzun.android.ui.view.description.DescriptionLogoView
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.extensions.color
import com.sibedge.yokodzun.android.utils.extensions.title
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SettingsManager.host
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.BattleStatus
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.dp128
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.ScreenManager
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.addLinearSeparator
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterSize
import ru.hnau.jutils.takeIfNotEmpty


class RaterLayerContentView(
    context: Context,
    battle: Battle
) : ScrollView(context) {

    init {
        isFillViewport = true
        addVerticalLayout {

            addView(
                DescriptionLogoView(
                    context = context,
                    preferredSize = dp128
                )
                    .apply {
                        applyLinearParams {
                            setTopMargin(SizeManager.LARGE_SEPARATION * 2)
                            setHorizontalMargins(SizeManager.DEFAULT_SEPARATION)
                        }
                        data = battle.description
                    }
            )

            addLabel(
                textSize = SizeManager.TEXT_20,
                gravity = HGravity.CENTER,
                text = battle.description.title.takeIfNotEmpty()?.toGetter()
                    ?: StringGetter(R.string.description_view_no_title_placeholder),
                textColor = ColorManager.PRIMARY,
                fontType = FontManager.REGULAR
            ) {
                applyLinearParams {
                    setHorizontalMargins(SizeManager.LARGE_SEPARATION)
                    setTopMargin(SizeManager.LARGE_SEPARATION)
                }
            }

            addLabel(
                textSize = SizeManager.TEXT_16,
                gravity = HGravity.CENTER,
                text = battle.description.description.takeIfNotEmpty()?.toGetter()
                    ?: StringGetter(R.string.description_view_no_description_placeholder),
                textColor = ColorManager.FG,
                fontType = FontManager.REGULAR
            ) {
                applyLinearParams {
                    setHorizontalMargins(SizeManager.LARGE_SEPARATION)
                    setTopMargin(SizeManager.SMALL_SEPARATION)
                }
            }

            addLabel(
                textSize = SizeManager.TEXT_16,
                gravity = HGravity.CENTER,
                text = battle.status.title,
                textColor = battle.status.color,
                fontType = FontManager.BOLD
            ) {
                applyLinearParams {
                    setHorizontalMargins(SizeManager.LARGE_SEPARATION)
                    setTopMargin(SizeManager.DEFAULT_SEPARATION)
                    setBottomMargin(SizeManager.DEFAULT_SEPARATION)
                }
            }

            addLinearSeparator()

            addCountView(
                title = StringGetter(R.string.battle_view_yokodzuns),
                color = ColorManager.ORANGE_TRIPLE,
                count = battle.yokodzunsIds.size
            ) {
                AppActivityConnector.showLayer({
                    ImmutableBattleYokodzunsLayer.newInstance(it, battle)
                })
            }

            addCountView(
                title = StringGetter(R.string.battle_view_parameters),
                color = ColorManager.GREEN_TRIPLE,
                count = battle.parameters.size
            ) {
                AppActivityConnector.showLayer({
                    ImmutableBattleParametersLayer.newInstance(it, battle)
                })
            }

            addCountView(
                title = StringGetter(R.string.battle_view_sections),
                color = ColorManager.PRIMARY_TRIPLE,
                count = battle.sections.size,
                onClick = this@RaterLayerContentView::rateOrViewSections
            )

            addLinearSeparator()

            if (battle.status == BattleStatus.IN_PROGRESS) {

                addButtonView(
                    text = StringGetter(R.string.dialog_rate),
                    info = YButtonInfo.LARGE_PRIMARY_BACKGROUND_SHADOW.copy(
                        backgroundColor = ColorManager.PURPLE_TRIPLE
                    ),
                    onClick = this@RaterLayerContentView::rateOrViewSections
                ) {
                    applyLinearParams {
                        setBottomMargin(SizeManager.DEFAULT_SEPARATION)
                    }
                }

            }

        }

    }

    private fun rateOrViewSections() {
        //TODO
    }

    private fun ViewGroup.addCountView(
        title: StringGetter,
        color: ColorTriple,
        count: Int,
        onClick: () -> Unit
    ) = addView(
        ClickableCountView(
            context = context,
            title = title,
            onClick = onClick,
            textSize = SizeManager.TEXT_16,
            titleCountSeparation = SizeManager.DEFAULT_SEPARATION,
            color = color
        ).apply {
            data = count
            applyPadding(SizeManager.SMALL_SEPARATION)
            applyLinearParams {
                setMatchParentWidth()
                setTopMargin(SizeManager.SMALL_SEPARATION)
                setHorizontalMargins(SizeManager.LARGE_SEPARATION - SizeManager.SMALL_SEPARATION)
            }
        }
    )

}