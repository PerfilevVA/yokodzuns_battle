package com.sibedge.yokodzun.android.ui.view.list.sections

import android.content.Context
import com.sibedge.yokodzun.android.ui.view.list.YListItemsDevider
import com.sibedge.yokodzun.android.ui.view.list.sections.content.SectionsList
import com.sibedge.yokodzun.android.ui.view.button.AdditionalButton
import com.sibedge.yokodzun.android.ui.view.list.sections.item.SectionView
import com.sibedge.yokodzun.android.ui.view.list.sections.item.TreeSection
import com.sibedge.yokodzun.android.utils.extensions.setBottomPaddingForPrimaryActionButtonDecoration
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.ui.view.list.base.*
import ru.hnau.androidutils.ui.view.utils.scroll.recycle_view.createOnRecyclerViewScrolledProducer
import ru.hnau.androidutils.ui.view.utils.scroll.recycle_view.createRecycleViewIsScrolledToTopProducer
import ru.hnau.jutils.me
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine


class SectionsTreeListView(
    context: Context,
    sectionsList: SectionsList,
    additionalButton: (Section) -> AdditionalButton.Info?
) : BaseList<TreeSection>(
    context = context,
    fixedSize = false,
    itemsProducer = Producer.combine(
        producer1 = sectionsList.sections,
        producer2 = sectionsList.openedSections,
        combiner = SectionsTreeUtils::sectionsListToTree
    ),
    viewWrappersCreator = {
        SectionView(context, additionalButton)
        { sectionsList.openedSections.switchSectionVisibility(it.section.id) }
    },
    orientation = BaseListOrientation.VERTICAL,
    calculateDiffInfo = BaseListCalculateDiffInfo.create(
        itemIdExtractor = TreeSection::key,
        itemContentExtractor = TreeSection::me,
        detectItemsMovesAfterUpdate = false
    ),
    itemsDecoration = YListItemsDevider.create(context)
) {

    private val onListScrolledProducer =
        createOnRecyclerViewScrolledProducer()

    val onListScrolledToTopProducer =
        createRecycleViewIsScrolledToTopProducer(onListScrolledProducer)

    init {
        setBottomPaddingForPrimaryActionButtonDecoration(sectionsList.sections)
    }

}