package com.sibedge.yokodzun.android.layers.students_group_tests.tree

import android.support.v7.widget.RecyclerView
import android.view.View
import com.sibedge.yokodzun.android.layers.students_group_tests.tree.item.TestTreeListCell


class TestsTreeListAdapterViewHolder(
    private val view: TestTreeListCell<*>
) : RecyclerView.ViewHolder(
    view
) {

    fun setContent(testsTreeNode: TestsTreeNode, position: Int) =
        view.setContent(testsTreeNode, position)

}