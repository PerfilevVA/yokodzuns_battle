package com.sibedge.yokodzun.android.ui.sections

import android.content.Context
import com.sibedge.yokodzun.android.ui.list.YListItemsDevider
import com.sibedge.yokodzun.android.ui.sections.item.AdditionalButton
import com.sibedge.yokodzun.android.ui.sections.item.SectionsTreeItemContentView
import com.sibedge.yokodzun.android.ui.sections.item.SectionsTreeItemView
import com.sibedge.yokodzun.android.ui.sections.item.TreeSection
import com.sibedge.yokodzun.android.utils.extensions.setBottomPaddingForPrimaryActionButtonDecoration
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.ui.view.list.base.*
import ru.hnau.androidutils.ui.view.utils.scroll.recycle_view.createOnRecyclerViewScrolledProducer
import ru.hnau.androidutils.ui.view.utils.scroll.recycle_view.createRecycleViewIsScrolledToTopProducer
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine


class SectionsTreeListView private constructor(
    context: Context,
    sections: Producer<List<Section>>,
    openedSections: OpenedSections,
    additionalButton: (Section) -> AdditionalButton.Info?
) : BaseList<TreeSection>(
    context = context,
    fixedSize = false,
    itemsProducer = Producer.combine(
        producer1 = sections,
        producer2 = openedSections,
        combiner = SectionsTreeUtils::sectionsListToTree
    ),
    viewWrappersCreator = { SectionsTreeItemView(context, additionalButton) { openedSections.switch(it.section.id) } },
    orientation = BaseListOrientation.VERTICAL,
    calculateDiffInfo = BaseListCalculateDiffInfo.create(
        itemIdExtractor = TreeSection::key,
        itemContentExtractor = TreeSection::key,
        detectItemsMovesAfterUpdate = false
    ),
    itemsDecoration = YListItemsDevider.create(context)
) {

    companion object {

        fun create(
            context: Context,
            sections: Producer<List<Section>>,
            additionalButton: (Section) -> AdditionalButton.Info?
        ) = SectionsTreeListView(
            context = context,
            openedSections = OpenedSections(),
            sections = sections,
            additionalButton = additionalButton
        )

    }

    private val onListScrolledProducer =
        createOnRecyclerViewScrolledProducer()

    val onListScrolledToTopProducer =
        createRecycleViewIsScrolledToTopProducer(onListScrolledProducer)

    init {
        setBottomPaddingForPrimaryActionButtonDecoration()
    }

}