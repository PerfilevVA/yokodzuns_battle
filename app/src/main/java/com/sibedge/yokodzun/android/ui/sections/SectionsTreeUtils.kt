package com.sibedge.yokodzun.android.ui.sections

import com.sibedge.yokodzun.android.ui.sections.item.TreeSection
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.jutils.ifFalse
import ru.hnau.jutils.ifNotNull
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList


object SectionsTreeUtils {

    private val OFFSET_COLORS = arrayOf(
        ColorManager.PRIMARY,
        ColorManager.RED,
        ColorManager.GREEN,
        ColorManager.ORANGE,
        ColorManager.PURPLE
    )

    fun getOffsetColor(offset: Int) =
        OFFSET_COLORS[offset % OFFSET_COLORS.size]

    fun sectionsListToTree(
        sections: List<Section>,
        openedSections: Collection<String>
    ): List<TreeSection> {
        val result = LinkedList<TreeSection>()
        sectionsListToTree(
            depth = 0,
            id = "",
            sections = sections.sortedBy { it.description.title },
            openedSections = openedSections,
            result = result
        )
        return ArrayList(result)
    }

    private fun sectionsListToTree(
        depth: Int,
        id: String,
        sections: List<Section>,
        openedSections: Collection<String>,
        result: MutableCollection<TreeSection>
    ) {
        result.find { it.section.parentSectionId == id }.ifNotNull {
            throw IllegalArgumentException("Sections tree contains circles")
        }
        sections.forEach { section ->
            (section.parentSectionId == id).ifFalse { return@forEach }
            result.add(
                TreeSection(
                    section = section,
                    depth = depth
                )
            )
            if (section.id in openedSections) {
                sectionsListToTree(
                    depth = depth + 1,
                    id = section.id,
                    sections = sections,
                    openedSections = openedSections,
                    result = result
                )
            }
        }
    }

}