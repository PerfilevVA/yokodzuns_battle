package com.sibedge.yokodzun.android.layers.section_info.list.item_view_wrapper

import android.content.Context
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerListCallback
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerListItem
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerListItemType
import com.sibedge.yokodzun.android.ui.cell.TitleCell


class SectionInfoLayerListTitleViewWrapper(
    context: Context,
    sectionInfoLayerListCallback: SectionInfoLayerListCallback
) : TitleCell<SectionInfoLayerListItem>(
    context = context,
    dataGetter = {
        (it.value as SectionInfoLayerListItemType).cellData.get(sectionInfoLayerListCallback)
    }
)