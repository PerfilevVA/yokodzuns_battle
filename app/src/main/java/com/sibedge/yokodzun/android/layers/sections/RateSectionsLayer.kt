package com.sibedge.yokodzun.android.layers.sections

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.login.LoginLayer
import com.sibedge.yokodzun.android.layers.rate.RateSectionLayer
import com.sibedge.yokodzun.android.layers.rater.RaterLayer
import com.sibedge.yokodzun.android.layers.sections.base.ImmutableSectionsLayer
import com.sibedge.yokodzun.android.layers.sections.edit.AdminEditSectionsLayer
import com.sibedge.yokodzun.android.layers.sections.edit.SectionsEditor
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.ui.view.list.sections.content.OpenedSections
import com.sibedge.yokodzun.android.utils.ColorTriple
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.fcm.FCMMessagesReceiver
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.notification.type.YNotificationBattleStopped
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.utils.runUi
import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.extensions.observeWhen


class RateSectionsLayer(
    context: Context
) : ImmutableSectionsLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = RateSectionsLayer(context).apply {
            this.battle = battle
            this.openedSections = OpenedSections()
        }

    }

    @LayerState
    override lateinit var battle: Battle

    @LayerState
    override lateinit var openedSections: OpenedSections

    override val additionalButtonInfoCreator = { section: Section ->
        (section.weight > 0).handle(
            onTrue = {
                AdditionalButton.Info(
                    icon = DrawableGetter(R.drawable.ic_rate),
                    color = ColorManager.PURPLE_TRIPLE
                ) {
                    AppActivityConnector.showLayer({
                        RateSectionLayer.newInstance(context, section)
                    })
                }
            },
            onFalse = { null }
        )
    }

    override val additionalButtonColor = ColorManager.PURPLE_TRIPLE

    init {
        FCMMessagesReceiver.observeWhen(isVisibleToUserProducer) { message ->
            if (message is YNotificationBattleStopped) {
                runUi { managerConnector.showLayer(RaterLayer(context), true) }
            }
        }
    }

}