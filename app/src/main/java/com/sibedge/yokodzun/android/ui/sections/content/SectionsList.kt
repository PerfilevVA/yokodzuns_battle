package com.sibedge.yokodzun.android.ui.sections.content

import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.jutils.producer.Producer


data class SectionsList(
    val sections: Producer<List<Section>>,
    val openedSections: OpenedSections = OpenedSections()
)