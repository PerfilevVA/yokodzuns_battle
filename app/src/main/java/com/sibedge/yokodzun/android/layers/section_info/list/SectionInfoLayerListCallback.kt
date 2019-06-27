package com.sibedge.yokodzun.android.layers.section_info.list

import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import ru.hnau.remote_teaching_common.data.test.TestSkeleton


interface SectionInfoLayerListCallback {

    fun onEditContentMDClick()

    fun onAddSubsectionClick()

    fun onAddTestClick()

    fun onSubsectionClick(subsection: SectionSkeleton)

    fun onSubsectionMenuClick(subsection: SectionSkeleton)

    fun onTestClick(test: TestSkeleton)

    fun onTestMenuClick(test: TestSkeleton)

}