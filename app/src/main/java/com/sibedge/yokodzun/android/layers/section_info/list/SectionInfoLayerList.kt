package com.sibedge.yokodzun.android.layers.section_info.list

import android.content.Context
import ru.hnau.androidutils.ui.view.list.base.BaseList
import ru.hnau.androidutils.ui.view.list.base.BaseListCalculateDiffInfo
import ru.hnau.jutils.producer.Producer
import com.sibedge.yokodzun.android.layers.section_info.list.item_view_wrapper.SectionInfoLayerListContentMDViewWrapper
import com.sibedge.yokodzun.android.layers.section_info.list.item_view_wrapper.SectionInfoLayerListSubsectionViewWrapper
import com.sibedge.yokodzun.android.layers.section_info.list.item_view_wrapper.SectionInfoLayerListTestViewWrapper
import com.sibedge.yokodzun.android.layers.section_info.list.item_view_wrapper.SectionInfoLayerListTitleViewWrapper
import com.sibedge.yokodzun.android.ui.list.RTListItemsDevider
import com.sibedge.yokodzun.android.utils.extensions.setBottomPaddingForMainActionButtonDecoration
import ru.hnau.remote_teaching_common.data.section.SectionInfo
import java.lang.IllegalArgumentException


class SectionInfoLayerList(
    context: Context,
    sectionInfoProducer: Producer<SectionInfo>,
    callback: SectionInfoLayerListCallback
) : BaseList<SectionInfoLayerListItem>(
    context = context,
    calculateDiffInfo = BaseListCalculateDiffInfo<SectionInfoLayerListItem>(
        itemsComparator = { item1, item2 -> item1.id == item2.id },
        itemsContentComparator = { item1, item2 -> item1.contentDescription == item2.contentDescription }
    ),
    itemTypeResolver = SectionInfoLayerListItem::type,
    viewWrappersCreator = { type ->
        when (type) {
            SectionInfoLayerListItem.ITEM_TYPE_TITLE ->
                SectionInfoLayerListTitleViewWrapper(context, callback)
            SectionInfoLayerListItem.ITEM_TYPE_CONTENT_MD ->
                SectionInfoLayerListContentMDViewWrapper(context)
            SectionInfoLayerListItem.ITEM_TYPE_SUBSECTION ->
                SectionInfoLayerListSubsectionViewWrapper(context, callback)
            SectionInfoLayerListItem.ITEM_TYPE_TEST ->
                SectionInfoLayerListTestViewWrapper(context, callback)
            else ->
                throw IllegalArgumentException("Incorrect item type $type")
        }
    },
    itemsProducer = sectionInfoProducer.map { (subsections, tests, contentMDs) ->

        ArrayList<SectionInfoLayerListItem>().apply {

            if (contentMDs.isNotEmpty()) {
                add(SectionInfoLayerListItem.createForTitle(SectionInfoLayerListItemType.CONTENT_MD))
                add(SectionInfoLayerListItem.createForContentMD(contentMDs))
            }

            if (subsections.isNotEmpty()) {
                add(SectionInfoLayerListItem.createForTitle(SectionInfoLayerListItemType.SUBSECTIONS))
                addAll(subsections.map(SectionInfoLayerListItem.Companion::createForSubsection))
            }

            if (tests.isNotEmpty()) {
                add(SectionInfoLayerListItem.createForTitle(SectionInfoLayerListItemType.TESTS))
                addAll(tests.map(SectionInfoLayerListItem.Companion::createForTest))
            }

        }

    },
    itemsDecoration = RTListItemsDevider.create(context)
) {

    init {
        setBottomPaddingForMainActionButtonDecoration()
    }

}