package com.sibedge.yokodzun.android.ui.sections.subsections

import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.AlwaysProducer


class OpenedSections : AlwaysProducer<Collection<String>>(),
    SubsectionsVisibilitySwitcher {

    override val value = LinkedHashSet<String>()

    override fun switchSectionVisibility(
        sectionId: String
    ) = (sectionId in value).handle(
        onTrue = { closeSection(sectionId) },
        onFalse = { openSection(sectionId) }
    )

    override fun openSection(sectionId: String) {
        value.add(sectionId)
        onValueChanged()
    }

    override fun closeSection(sectionId: String) {
        value.remove(sectionId)
        onValueChanged()
    }

}