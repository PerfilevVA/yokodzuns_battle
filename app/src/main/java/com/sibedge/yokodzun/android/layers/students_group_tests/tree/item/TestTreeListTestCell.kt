package com.sibedge.yokodzun.android.layers.students_group_tests.tree.item

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.toGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.students_group_tests.tree.TestsTreeNode
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.remote_teaching_common.data.section.SectionSkeleton
import ru.hnau.remote_teaching_common.data.test.TestSkeleton


class TestTreeListTestCell(
    context: Context,
    onClick: suspend (TestsTreeNode, TestSkeleton) -> Unit
) : TestTreeListCell<TestSkeleton>(
    context = context,
    titleColor = ColorManager.PRIMARY,
    testTreeNodeToType = { it.test!! },
    titleResolver = { it.title.toGetter() },
    onClick = onClick
)


