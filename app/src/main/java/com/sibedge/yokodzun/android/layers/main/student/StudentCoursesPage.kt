package com.sibedge.yokodzun.android.layers.main.student

import android.content.Context
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.jutils.getter.base.map
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.SectionsInfoManager
import com.sibedge.yokodzun.android.layers.student_section_info.StudentSectionInfoLayer
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.list.base.ItemCellListContaner
import com.sibedge.yokodzun.android.utils.extensions.getLineCellData
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton


class StudentCoursesPage(
    context: Context
) : ItemCellListContaner<SectionSkeleton>(
    context = context,
    invalidator = SECTIONS_MANAGER::invalidate,
    onClick = { subsection ->
        AppActivityConnector.showLayer(
            layerBuilder = { context ->
                StudentSectionInfoLayer.newInstance(context, subsection)
            }
        )
    },
    producer = SECTIONS_MANAGER.map { getter ->
        getter.map { sectionInfo ->
            sectionInfo.subsections
        }
    },
    idGetter = { sectionSkeleton ->
        sectionSkeleton.uuid
    },
    onEmptyListInfoViewGenerator = {
        EmptyInfoView(
            context = context,
            text = StringGetter(R.string.student_main_view_courses_page_empty_info_title)
        )
    },
    cellDataGetter = { sectionSkeleton ->
        sectionSkeleton.getLineCellData(onMenuClick = null)
    }
) {

    companion object {

        private val SECTIONS_MANAGER =
            SectionsInfoManager.COURSES

    }

}