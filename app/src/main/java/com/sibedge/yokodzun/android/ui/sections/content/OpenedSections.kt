package com.sibedge.yokodzun.android.ui.sections.content

import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.AlwaysProducer


class OpenedSections : AlwaysProducer<Collection<String>>() {

    override val value = LinkedHashSet<String>()

    fun switchSectionVisibility(
        sectionId: String
    ) = (sectionId in value).handle(
        onTrue = { closeSection(sectionId) },
        onFalse = { openSection(sectionId) }
    )

    fun openSection(sectionId: String) {
        value.add(sectionId)
        onValueChanged()
    }

    fun closeSection(sectionId: String) {
        value.remove(sectionId)
        onValueChanged()
    }

}