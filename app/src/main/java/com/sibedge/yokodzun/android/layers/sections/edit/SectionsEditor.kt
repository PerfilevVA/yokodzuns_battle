package com.sibedge.yokodzun.android.layers.sections.edit

import com.sibedge.yokodzun.android.ui.view.list.sections.content.SectionsList
import com.sibedge.yokodzun.android.utils.Utils
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.jutils.handle
import ru.hnau.jutils.ifTrue
import ru.hnau.jutils.producer.ActualProducerSimple
import java.util.concurrent.CopyOnWriteArrayList


class SectionsEditor(
    private val battle: Battle
) : SectionsEditingCallback {

    private val sectionsProducer = ActualProducerSimple<List<Section>>(battle.sections)

    val sectionsList =
        SectionsList(sectionsProducer)

    val sections get() = sectionsProducer.currentState

    override fun addSubsection(
        parentId: String
    ) {
        update {
            plus(
                Section(
                    id = Utils.genUUID(),
                    parentSectionId = parentId
                )
            )
        }
        sectionsList.openedSections.openSection(parentId)
    }

    override fun updateDescription(id: String, newDescription: Description) =
        update(id) { copy(description = newDescription) }

    override fun updateWeight(id: String, newWeight: Int) =
        update(id) { copy(weight = newWeight) }

    override fun remove(
        id: String
    ) = update {
        CopyOnWriteArrayList(this).also { remove(it, id) }
    }

    private fun remove(
        sections: MutableList<Section>,
        id: String
    ) {
        sections.forEach { section ->
            (section.parentSectionId == id).ifTrue { remove(sections, section.id) }
        }
        sections.removeIf { it.id == id }
    }

    private fun update(updateSections: List<Section>.() -> List<Section>) =
        sectionsProducer.updateState(sectionsProducer.currentState.updateSections())

    private fun update(
        id: String,
        updateSection: Section.() -> Section
    ) = update {
        map { section ->
            (section.id == id).handle(
                onTrue = { section.updateSection() },
                onFalse = { section }
            )
        }
    }

}