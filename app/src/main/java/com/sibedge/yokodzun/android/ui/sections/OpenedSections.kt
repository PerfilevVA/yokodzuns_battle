package com.sibedge.yokodzun.android.ui.sections

import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.AlwaysProducer


class OpenedSections : AlwaysProducer<Collection<String>>() {

    override val value = LinkedHashSet<String>()

    fun switch(
        sectionId: String
    ) {
        (sectionId in value).handle(
            onTrue = { value.remove(sectionId) },
            onFalse = { value.add(sectionId) }
        )
        onValueChanged()
    }

}