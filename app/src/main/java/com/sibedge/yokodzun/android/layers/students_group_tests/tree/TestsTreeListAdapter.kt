package com.sibedge.yokodzun.android.layers.students_group_tests.tree

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.hnau.jutils.getter.base.get
import com.sibedge.yokodzun.android.data.SectionsInfoManager
import com.sibedge.yokodzun.android.layers.students_group_tests.tree.item.TestTreeListSectionCell
import com.sibedge.yokodzun.android.layers.students_group_tests.tree.item.TestTreeListTestCell
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import ru.hnau.remote_teaching_common.data.test.TestSkeleton


class TestsTreeListAdapter(
    private val context: Context,
    rootSections: List<SectionSkeleton>,
    private val onTestClick: (TestSkeleton) -> Unit
) : RecyclerView.Adapter<TestsTreeListAdapterViewHolder>() {

    private val nodes = TestTreeListNodes(rootSections)

    override fun onCreateViewHolder(
        container: ViewGroup,
        type: Int
    ) = TestsTreeListAdapterViewHolder(
        when (type) {
            TestsTreeNode.ITEM_TYPE_SECTION -> TestTreeListSectionCell(context, this::onSectionClick)
            else -> TestTreeListTestCell(context) { _, test -> onTestClick(test) }
        }
    )

    override fun getItemCount() =
        nodes.weight

    override fun onBindViewHolder(
        viewHolder: TestsTreeListAdapterViewHolder,
        position: Int
    ) = viewHolder.setContent(
        nodes[position],
        position
    )

    override fun getItemViewType(position: Int) =
        nodes[position].type

    private suspend fun onSectionClick(
        node: TestsTreeNode,
        section: SectionSkeleton
    ) {
        val firstSubitemPosition = nodes.getNodePosition(node) + 1
        val subnodesCount = node.weight - 1
        if (subnodesCount > 0) {
            node.subnodes = emptyList()
            notifyItemRangeRemoved(firstSubitemPosition, subnodesCount)
            return
        }

        val sectionInfo = SectionsInfoManager[section.uuid].wait().get()
        val sectionsSubnodes = sectionInfo.subsections.map { subsection ->
            TestsTreeNode.createSection(
                level = node.level + 1,
                section = subsection
            )
        }
        val testsSubnodes = sectionInfo.tests.map { test ->
            TestsTreeNode.createTest(
                level = node.level + 1,
                test = test
            )
        }
        node.subnodes = sectionsSubnodes + testsSubnodes
        notifyItemRangeInserted(firstSubitemPosition, node.subnodes.size)
    }

}