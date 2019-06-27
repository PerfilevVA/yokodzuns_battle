package com.sibedge.yokodzun.android.layers.students_group_tests.tree

import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import java.lang.IllegalStateException


class TestTreeListNodes(
    rootSections: List<SectionSkeleton>
) {

    private val nodes =
        rootSections.map { TestsTreeNode.Companion.createSection(0, it) }

    val weight: Int
        get() = nodes.sumBy(TestsTreeNode::weight)

    operator fun get(position: Int): TestsTreeNode {
        var temp = position
        nodes.forEach { subnode ->
            val weight = subnode.weight
            if (temp < weight) {
                return subnode.getNode(temp)
            }
            temp -= weight
        }
        throw IllegalStateException("Node with position $position not found")
    }

    fun getNodePosition(node: TestsTreeNode): Int {
        nodes.forEachIndexed { i, subnode ->
            subnode.getNodePosition(node)?.let { nodeResult ->
                return i + nodeResult
            }
        }
        throw IllegalStateException("Position on node $node not found")
    }

}