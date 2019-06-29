package com.sibedge.yokodzun.android.ui.sections

import android.content.Context
import com.sibedge.yokodzun.android.ui.list.YListItemsDevider
import com.sibedge.yokodzun.android.ui.sections.item.AdditionalButton
import com.sibedge.yokodzun.android.ui.sections.item.SectionsTreeItemView
import com.sibedge.yokodzun.android.ui.sections.item.TreeSection
import com.sibedge.yokodzun.android.ui.sections.subsections.OpenedSections
import com.sibedge.yokodzun.android.ui.sections.subsections.SubsectionsVisibilitySwitcher
import com.sibedge.yokodzun.android.utils.extensions.setBottomPaddingForPrimaryActionButtonDecoration
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.ui.view.list.base.*
import ru.hnau.androidutils.ui.view.utils.scroll.recycle_view.createOnRecyclerViewScrolledProducer
import ru.hnau.androidutils.ui.view.utils.scroll.recycle_view.createRecycleViewIsScrolledToTopProducer
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine
import ru.hnau.jutils.producer.extensions.filterUnique


class SectionsTreeListView private constructor(
    context: Context,
    openedSections: OpenedSections,
    itemsProducer: Producer<List<TreeSection>>,
    additionalButton: (Section) -> AdditionalButton.Info?
) : BaseList<TreeSection>(
    context = context,
    fixedSize = false,
    itemsProducer = itemsProducer,
    viewWrappersCreator = {
        SectionsTreeItemView(context, additionalButton) { openedSections.switchSectionVisibility(it.section.id) }
    },
    orientation = BaseListOrientation.VERTICAL,
    calculateDiffInfo = BaseListCalculateDiffInfo.create(
        itemIdExtractor = TreeSection::key,
        itemContentExtractor = TreeSection::key,
        detectItemsMovesAfterUpdate = false
    ),
    itemsDecoration = YListItemsDevider.create(context)
), SubsectionsVisibilitySwitcher by openedSections {

    companion object {

        fun create(
            context: Context,
            sections: Producer<List<Section>>,
            additionalButton: (Section) -> AdditionalButton.Info?
        ): SectionsTreeListView {
            val openedSections = OpenedSections()
            return SectionsTreeListView(
                context = context,
                openedSections = openedSections,
                itemsProducer = Producer.combine(
                    producer1 = sections,
                    producer2 = openedSections,
                    combiner = SectionsTreeUtils::sectionsListToTree
                ),
                additionalButton = additionalButton
            )
        }

    }

    private val onListScrolledProducer =
        createOnRecyclerViewScrolledProducer()

    val onListScrolledToTopProducer =
        createRecycleViewIsScrolledToTopProducer(onListScrolledProducer)

    init {
        setBottomPaddingForPrimaryActionButtonDecoration(itemsProducer)
    }

}