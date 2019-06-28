package com.sibedge.yokodzun.android.layers

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.sections.SectionsTreeListView
import com.sibedge.yokodzun.android.ui.sections.item.AdditionalButton
import com.sibedge.yokodzun.android.utils.Utils
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.view_presenter.*
import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.StateProducerSimple
import ru.hnau.jutils.producer.extensions.not


class SectionsLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = SectionsLayer(context).apply {
            this.battle = battle
        }

    }

    @LayerState
    private lateinit var battle: Battle

    override val title get() = battle.entityNameWithTitle

    private val sectionsProducer
            by lazy { StateProducerSimple(battle.sections) }

    override fun afterCreate() {
        super.afterCreate()

        val list by lazy {
            SectionsTreeListView.create(
                context = context,
                sections = sectionsProducer,
                additionalButton = { section ->
                    AdditionalButton.Info(
                        icon = DrawableGetter(R.drawable.ic_options_white),
                        action = {
                            addSection(section.id)
                        }
                    )
                }
            )
        }

        val emptyInfoView by lazy {
            EmptyInfoView(
                context = context,
                text = StringGetter(R.string.sections_layer_no_sections_title),
                button = StringGetter(R.string.sections_layer_no_sections_add_section) to this::addRootSection
            )
        }

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
                    presentingViewProducer = sectionsProducer.map { sections ->
                        sections.isEmpty().handle(
                            onTrue = { emptyInfoView },
                            onFalse = { list }
                        ).toPresentingInfo(ColorManager.DEFAULT_PRESENTING_VIEW_PROPERTIES)
                    }
                )

                addPrimaryActionButton(
                    icon = DrawableGetter(R.drawable.ic_add_white),
                    title = StringGetter(R.string.sections_layer_no_sections_add_section),
                    needShowTitle = list.onListScrolledToTopProducer.not(),
                    onClick = this@SectionsLayer::addRootSection
                ) {
                    applyFrameParams {
                        setMargins(SizeManager.DEFAULT_SEPARATION)
                        setEndBottomGravity()
                    }
                }

            }

        }

    }

    private fun addRootSection() =
        addSection("")

    private fun addSection(
        parentId: String
    ) {
        val oldSectionsList = sectionsProducer.currentState ?: return
        val newSection = Section(
            id = Utils.genUUID(),
            parentSectionId = parentId
        )
        sectionsProducer.updateState(oldSectionsList + newSection)
    }

}