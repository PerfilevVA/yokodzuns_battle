package com.sibedge.yokodzun.android.layers.student_section_info.list

import android.content.Context
import ru.hnau.androidutils.ui.view.list.base.BaseList
import ru.hnau.androidutils.ui.view.list.base.BaseListCalculateDiffInfo
import ru.hnau.androidutils.ui.view.list.base.BaseListOrientation
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.student_section_info.list.item_view_wrapper.StudentSectionInfoLayerListContentMDViewWrapper
import com.sibedge.yokodzun.android.layers.student_section_info.list.item_view_wrapper.StudentSectionInfoLayerListSubsectionViewWrapper
import com.sibedge.yokodzun.android.ui.list.RTListItemsDevider
import ru.hnau.remote_teaching_common.data.section.SectionInfo
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton


class StudentSectionInfoLayerList(
    context: Context,
    sectionInfoProducer: Producer<SectionInfo>,
    onSubsectionClick: (SectionSkeleton) -> Unit
) : BaseList<StudentSectionInfoLayerListItem>(
    context = context,
    calculateDiffInfo = BaseListCalculateDiffInfo<StudentSectionInfoLayerListItem>(
        itemsComparator = { item1, item2 -> item1.id == item2.id },
        itemsContentComparator = { item1, item2 -> item1.contentDescription == item2.contentDescription }
    ),
    itemTypeResolver = StudentSectionInfoLayerListItem::type,
    orientation = BaseListOrientation.VERTICAL,
    itemsProducer = sectionInfoProducer.map { sectionInfo ->
        val contentMDList = sectionInfo.contentMD.takeIfNotEmpty()
            ?.let(StudentSectionInfoLayerListItem.Companion::createForContentMD)
            .toSingleItemOrEmptyList()

        val subsectionsList = sectionInfo.subsections
            .map(StudentSectionInfoLayerListItem.Companion::createForSubsection)

        contentMDList + subsectionsList
    },
    viewWrappersCreator = { type ->
        when (type) {
            StudentSectionInfoLayerListItem.ITEM_TYPE_CONTENT_MD ->
                StudentSectionInfoLayerListContentMDViewWrapper(context)
            else ->
                StudentSectionInfoLayerListSubsectionViewWrapper(context, onSubsectionClick)
        }
    },
    itemsDecoration = RTListItemsDevider.create(context)
)