package com.sibedge.yokodzun.android.ui.sections.subsections

import ru.hnau.jutils.handle


interface SubsectionsVisibilitySwitcher {

    fun switchSectionVisibility(sectionId: String)

    fun openSection(sectionId: String)

    fun closeSection(sectionId: String)

}