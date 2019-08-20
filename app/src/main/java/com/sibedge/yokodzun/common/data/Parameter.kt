package com.sibedge.yokodzun.common.data

import com.sibedge.yokodzun.common.data.helpers.Description
import com.sibedge.yokodzun.common.data.helpers.ListItem


data class Parameter(
        override val id: String = "",
        val description: Description = Description()
) : ListItem<String, String> {

    override val sortKey get() = description.title + "_" + id

}