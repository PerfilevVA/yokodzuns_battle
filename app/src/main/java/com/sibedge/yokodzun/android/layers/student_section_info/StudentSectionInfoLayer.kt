package com.sibedge.yokodzun.android.layers.student_section_info

import android.content.Context
import android.view.View
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.auto_swipe_refresh_view.addAutoSwipeRefreshView
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.StateProducerSimple
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.SectionsInfoManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.student_section_info.list.StudentSectionInfoLayerList
import com.sibedge.yokodzun.android.ui.addSuspendPresenter
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.remote_teaching_common.data.section.SectionInfo
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton


class StudentSectionInfoLayer(
    context: Context
) : AppLayer(
    context = context
) {
    companion object {

        fun newInstance(
            context: Context,
            section: SectionSkeleton
        ) = StudentSectionInfoLayer(context).apply {
            sectionSkeleton = section
        }

    }

    @LayerState
    private lateinit var sectionSkeleton: SectionSkeleton

    override val title: StringGetter by lazy {
        StringGetter(R.string.student_section_info_layer_title, sectionSkeleton.title)
    }

    private val sectionInfoManager: SectionsInfoManager
            by lazy { SectionsInfoManager[sectionSkeleton.uuid] }

    private val sectionInfoProducer = StateProducerSimple<SectionInfo>()

    private val emptyInfoView by lazy {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.student_section_info_layer_empty_info_title)
        )
    }

    private val listView by lazy {
        StudentSectionInfoLayerList(
            context = context,
            onSubsectionClick = this@StudentSectionInfoLayer::onSubsectionClick,
            sectionInfoProducer = sectionInfoProducer
        )
    }

    override fun afterCreate() {
        super.afterCreate()
        content {

            addAutoSwipeRefreshView(
                color = ColorManager.PRIMARY,
                updateContent = sectionInfoManager::invalidate
            ) {

                addSuspendPresenter(
                    producer = sectionInfoManager as Producer<GetterAsync<Unit, SectionInfo>>,
                    invalidator = sectionInfoManager::invalidate,
                    contentViewGenerator = this@StudentSectionInfoLayer::generateContentView
                )

            }

        }
    }

    private fun generateContentView(
        sectionInfo: SectionInfo
    ): View {
        sectionInfoProducer.updateState(sectionInfo)
        if (sectionInfo.contentMD.isBlank() && sectionInfo.subsections.isEmpty()) {
            return emptyInfoView
        }
        return listView
    }

    private fun onSubsectionClick(
        subsection: SectionSkeleton
    ) = AppActivityConnector.showLayer(
        layerBuilder = { context ->
            newInstance(context, subsection)
        }
    )

}