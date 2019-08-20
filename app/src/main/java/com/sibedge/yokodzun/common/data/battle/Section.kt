package com.sibedge.yokodzun.common.data.battle

import com.sibedge.yokodzun.common.data.helpers.Description
import com.sibedge.yokodzun.common.data.helpers.ListItem


data class Section(
        override val id: String = "",
        val parentSectionId: String = "",
        val description: Description = Description(),
        val weight: Int = 0
) : ListItem<String, String> {

    override val sortKey get() = description.title

    val ratable get() = weight > 0

}