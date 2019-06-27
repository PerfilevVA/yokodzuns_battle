package com.sibedge.yokodzun.android.layers.students_group_tests.tree

import ru.hnau.jutils.ifTrue
import ru.hnau.jutils.takeIfPositive
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import ru.hnau.remote_teaching_common.data.test.TestSkeleton
import java.lang.IllegalStateException


class TestsTreeNode(
    val level: Int,
    val section: SectionSkeleton? = null,
    val test: TestSkeleton? = null,
    var subnodes: List<TestsTreeNode> = emptyList()
) {


    companion object {

        const val ITEM_TYPE_SECTION = 0
        const val ITEM_TYPE_TEST = 1

        fun createSection(
            level: Int,
            section: SectionSkeleton
        ) = TestsTreeNode(
            level = level,
            section = section
        )

        fun createTest(
            level: Int,
            test: TestSkeleton
        ) = TestsTreeNode(
            level = level,
            test = test
        )

    }

    val weight: Int
        get() = subnodes.sumBy(TestsTreeNode::weight) + 1

    val type = when {
        section != null -> ITEM_TYPE_SECTION
        else -> ITEM_TYPE_TEST
    }

    fun getNode(offset: Int): TestsTreeNode =
        offset.takeIfPositive()?.let {
            var temp = it - 1
            subnodes.forEach { subnode ->
                val weight = subnode.weight
                if (temp < weight) {
                    return@let subnode.getNode(temp)
                }
                temp -= weight
            }
            throw IllegalStateException("Node with offset $offset not found")
        } ?: this

    fun getNodePosition(node: TestsTreeNode): Int? {
        (node == this).ifTrue { return 0 }
        subnodes.forEachIndexed { i, subnode ->
            subnode.getNodePosition(node)?.let { subnodeResult ->
                return 1 + i + subnodeResult
            }
        }
        return null
    }


}