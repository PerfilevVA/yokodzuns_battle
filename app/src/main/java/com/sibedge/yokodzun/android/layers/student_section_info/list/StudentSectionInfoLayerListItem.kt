package com.sibedge.yokodzun.android.layers.student_section_info.list

import ru.hnau.jutils.helpers.Box
import ru.hnau.jutils.helpers.toBox
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton


data class StudentSectionInfoLayerListItem private constructor(
    private val contentMD: Box<String>? = null,
    private val subsection: Box<SectionSkeleton>? = null
) {

    companion object {

        const val ITEM_TYPE_CONTENT_MD = 0
        const val ITEM_TYPE_SUBSECTION = 1

        fun createForContentMD(contentMD: String) =
            StudentSectionInfoLayerListItem(contentMD = contentMD.toBox())

        fun createForSubsection(subsection: SectionSkeleton) =
            StudentSectionInfoLayerListItem(subsection = subsection.toBox())

    }

    val value = when {
        contentMD != null -> contentMD
        else -> subsection!!
    }.value

    val id = when {
        subsection != null -> subsection.value.uuid
        else -> "ContentMD"
    }

    val contentDescription = when {
        contentMD != null -> contentMD.value
        subsection != null -> subsection.value.toString()
        else -> ""
    }

    val type = when {
        contentMD != null -> ITEM_TYPE_CONTENT_MD
        else -> ITEM_TYPE_SUBSECTION
    }

}