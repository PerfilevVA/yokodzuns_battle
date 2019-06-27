package com.sibedge.yokodzun.android.layers.section_info.list

import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.jutils.getter.Getter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.line.LineCell


enum class SectionInfoLayerListItemType(
    val cellData: Getter<SectionInfoLayerListCallback, LineCell.Data<SectionInfoLayerListItem>>
) {

    CONTENT_MD(
        cellData = Getter { sectionInfoLayerListCallback ->
            LineCell.Data(
                title = StringGetter(R.string.section_info_layer_list_title_content_md),
                additionalActionButtonInfo = CellAdditionalActionButton.Info(
                    icon = DrawableGetter(R.drawable.ic_edit_white),
                    onClick = { sectionInfoLayerListCallback.onEditContentMDClick() }
                )
            )
        }
    ),

    SUBSECTIONS(
        cellData = Getter { sectionInfoLayerListCallback ->
            LineCell.Data(
                title = StringGetter(R.string.section_info_layer_list_title_subsections),
                additionalActionButtonInfo = CellAdditionalActionButton.Info(
                    icon = DrawableGetter(R.drawable.ic_add_white),
                    onClick = { sectionInfoLayerListCallback.onAddSubsectionClick() }
                )
            )
        }
    ),

    TESTS(
        cellData = Getter { sectionInfoLayerListCallback ->
            LineCell.Data(
                title = StringGetter(R.string.section_info_layer_list_title_tests),
                additionalActionButtonInfo = CellAdditionalActionButton.Info(
                    icon = DrawableGetter(R.drawable.ic_add_white),
                    onClick = { sectionInfoLayerListCallback.onAddTestClick() }
                )
            )
        }
    )
}