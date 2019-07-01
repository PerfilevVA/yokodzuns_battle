package com.sibedge.yokodzun.android.layers.sections.edit

import android.content.Context
import android.view.ViewGroup
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.BattlesDataManager
import com.sibedge.yokodzun.android.layers.sections.base.SectionsLayer
import com.sibedge.yokodzun.android.ui.view.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.sections.SectionsTreeListView
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.jutils.producer.extensions.not


class AdminEditSectionsLayer(
    context: Context
) : SectionsLayer(
    context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = AdminEditSectionsLayer(context).apply {
            this.battle = battle
            this.editor = SectionsEditor(battle)
        }

    }

    @LayerState
    override lateinit var battle: Battle

    @LayerState
    private lateinit var editor: SectionsEditor

    override val sectionsList get() = editor.sectionsList

    override fun createAdditionalButtonInfo(
        section: Section
    ) = AdditionalButton.Info(
        icon = DrawableGetter(R.drawable.ic_options_white),
        action = {
            AdminEditSectionsLayerUtils.showSectionActions(
                section = section,
                callback = editor
            )
        }
    )

    override val emptyInfoView by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.sections_layer_no_sections_title),
            button = StringGetter(R.string.sections_layer_no_sections_add_section) to editor::addRootSection
        )
    }

    override fun ViewGroup.configureView(
        listView: SectionsTreeListView
    ) {
        addPrimaryActionButton(
            icon = DrawableGetter(R.drawable.ic_add_white),
            title = StringGetter(R.string.sections_layer_no_sections_add_section),
            needShowTitle = listView.onListScrolledToTopProducer.not(),
            onClick = editor::addRootSection
        ) {
            applyFrameParams {
                setMargins(SizeManager.DEFAULT_SEPARATION)
                setEndBottomGravity()
            }
        }
    }

    override fun handleGoBack(): Boolean {
        uiJobLocked {
            BattlesDataManager.updateSections(battle.id, editor.sections)
            managerConnector.goBack()
        }
        return true
    }

}