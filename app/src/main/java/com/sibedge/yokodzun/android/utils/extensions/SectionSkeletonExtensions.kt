package com.sibedge.yokodzun.android.utils.extensions

import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.toGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.line.LineCell
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton


fun SectionSkeleton.getLineCellData(
    onMenuClick: (() -> Unit)?
) = LineCell.Data<SectionSkeleton>(
    title = title.toGetter(),
    additionalActionButtonInfo = onMenuClick?.let { onMenuClickInner ->
        CellAdditionalActionButton.Info(
            icon = DrawableGetter(R.drawable.ic_options_primary),
            onClick = onMenuClickInner
        )
    }
)