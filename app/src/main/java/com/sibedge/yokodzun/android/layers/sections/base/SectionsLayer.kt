package com.sibedge.yokodzun.android.layers.sections.base

import android.content.Context
import android.view.ViewGroup
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.sections.SectionsTreeListView
import com.sibedge.yokodzun.android.ui.view.list.sections.content.SectionsList
import com.sibedge.yokodzun.android.ui.view.list.sections.item.AdditionalButton
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.view_presenter.*
import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.extensions.toProducer


abstract class SectionsLayer(
    context: Context
) : AppLayer(
    context = context
) {

    protected abstract val battle: Battle

    override val title get() = battle.entityNameWithTitle

    protected open val sectionsList
            by lazy { SectionsList(battle.sections.toProducer()) }

    private val listView by lazy {
        SectionsTreeListView(
            context = context,
            sectionsList = sectionsList,
            additionalButton = this::createAdditionalButtonInfo
        )
    }

    protected abstract fun createAdditionalButtonInfo(section: (Section)): AdditionalButton.Info?

    protected abstract val emptyInfoView: EmptyInfoView

    protected open fun ViewGroup.configureView(listView: SectionsTreeListView) {}

    override fun afterCreate() {
        super.afterCreate()

        content {

            addFrameLayout {

                applyLinearParams {
                    setStretchedHeight()
                    setMatchParentWidth()
                }

                addPresenter(
                    presenterViewInfo = PresenterViewInfo(
                        horizontalSizeInterpolator = SizeInterpolator.MAX,
                        verticalSizeInterpolator = SizeInterpolator.MAX
                    ),
                    presentingViewProducer = sectionsList.sections.map { sections ->
                        sections.isEmpty().handle(
                            onTrue = { emptyInfoView },
                            onFalse = { listView }
                        ).toPresentingInfo(ColorManager.DEFAULT_PRESENTING_VIEW_PROPERTIES)
                    }
                )

                configureView(listView)

            }

        }

    }

}