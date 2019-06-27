package com.sibedge.yokodzun.android.layers.section_info.list

import ru.hnau.jutils.helpers.Box
import ru.hnau.jutils.helpers.toBox
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import ru.hnau.remote_teaching_common.data.test.TestSkeleton


class SectionInfoLayerListItem private constructor(
    private val itemType: Box<SectionInfoLayerListItemType>? = null,
    private val contentMD: Box<String>? = null,
    private val subsection: Box<SectionSkeleton>? = null,
    private val test: Box<TestSkeleton>? = null
) {

    companion object {

        const val ITEM_TYPE_TITLE = 0
        const val ITEM_TYPE_CONTENT_MD = 1
        const val ITEM_TYPE_SUBSECTION = 2
        const val ITEM_TYPE_TEST = 3

        fun createForTitle(itemType: SectionInfoLayerListItemType) =
            SectionInfoLayerListItem(itemType = itemType.toBox())

        fun createForContentMD(contentMD: String) =
            SectionInfoLayerListItem(contentMD = contentMD.toBox())

        fun createForSubsection(subsection: SectionSkeleton) =
            SectionInfoLayerListItem(subsection = subsection.toBox())

        fun createForTest(test: TestSkeleton) =
            SectionInfoLayerListItem(test = test.toBox())

    }

    val value = when {
        itemType != null -> itemType
        contentMD != null -> contentMD
        test != null -> test
        else -> subsection!!
    }.value

    val id = when {
        itemType != null -> itemType.value.name
        subsection != null -> subsection.value.uuid
        test != null -> test.value.uuid
        else -> "ContentMD"
    }

    val contentDescription = when {
        contentMD != null -> contentMD.value
        subsection != null -> subsection.value.toString()
        test != null -> test.value.toString()
        else -> ""
    }

    val type = when {
        itemType != null -> ITEM_TYPE_TITLE
        contentMD != null -> ITEM_TYPE_CONTENT_MD
        test != null -> ITEM_TYPE_TEST
        else -> ITEM_TYPE_SUBSECTION
    }

}