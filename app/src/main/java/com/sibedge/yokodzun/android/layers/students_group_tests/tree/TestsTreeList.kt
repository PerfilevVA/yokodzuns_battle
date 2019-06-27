package com.sibedge.yokodzun.android.layers.students_group_tests.tree

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import ru.hnau.androidutils.ui.view.list.base.BaseList
import ru.hnau.androidutils.ui.view.list.base.BaseListItemsDivider
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import ru.hnau.remote_teaching_common.data.test.TestSkeleton


class TestsTreeList(
    context: Context,
    rootSections: List<SectionSkeleton>,
    onTestClick: (TestSkeleton) -> Unit
) : RecyclerView(
    context
) {

    init {
        adapter = TestsTreeListAdapter(context, rootSections, onTestClick)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(BaseListItemsDivider(context))
        setHasFixedSize(true)
    }

}