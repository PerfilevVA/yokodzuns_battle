package com.sibedge.yokodzun.android.utils.extensions

import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.line.LineCell
import ru.hnau.remote_teaching_common.data.StudentsGroup


val StudentsGroup.sortKey: String
    get() = "${archived}_${name}"

fun StudentsGroup.getLineCellData(
    onMenuClick: () -> Unit
) = LineCell.Data<StudentsGroup>(
    title = name.toGetter(),
    subtitle = StringGetter(
        if (archived) {
            R.string.students_group_status_archived
        } else {
            R.string.students_group_status_unarchived
        }
    ),
    active = !archived,
    additionalActionButtonInfo = CellAdditionalActionButton.Info(
        icon = DrawableGetter(R.drawable.ic_options_primary),
        onClick = onMenuClick
    )
)