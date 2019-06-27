package com.sibedge.yokodzun.android.layers.student_section_info.list.item_view_wrapper

import android.content.Context
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.toGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerListCallback
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerListItem
import com.sibedge.yokodzun.android.layers.student_section_info.list.StudentSectionInfoLayerListItem
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.ItemCell
import com.sibedge.yokodzun.android.ui.cell.line.LineCell
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton


class StudentSectionInfoLayerListSubsectionViewWrapper(
    context: Context,
    onSubsectionClick: (SectionSkeleton) -> Unit
) : ItemCell<StudentSectionInfoLayerListItem>(
    context = context,
    onClick = { item ->
        val subsection = item.value as SectionSkeleton
        onSubsectionClick(subsection)
    },
    dataGetter = { item ->
        val subsection = item.value as SectionSkeleton
        LineCell.Data<StudentSectionInfoLayerListItem>(
            title = subsection.title.toGetter()
        )
    }
)