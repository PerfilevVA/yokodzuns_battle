package com.sibedge.yokodzun.android.layers.sections.edit

import com.sibedge.yokodzun.common.data.battle.Section
import com.sibedge.yokodzun.common.data.helpers.Description


interface SectionsEditingCallback {

    fun addSubsection(parentId: String)

    fun addRootSection() = addSubsection("")

    fun updateDescription(id: String, newDescription: Description)

    fun updateWeight(id: String, newWeight: Int)

    fun remove(id: String)

}