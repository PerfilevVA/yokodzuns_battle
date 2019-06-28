package com.sibedge.yokodzun.android.ui.sections.item

import com.sibedge.yokodzun.common.data.battle.Section


data class TreeSection(
    val section: Section,
    val depth: Int
) {

    val key = "${depth}_{section.id}"

}