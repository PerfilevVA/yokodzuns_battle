package com.sibedge.yokodzun.android.layers.test_info

import android.content.Context
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.entity.TestTaskWithId
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.properties.PropertiesCell
import com.sibedge.yokodzun.android.ui.cell.properties.PropertiesCellPropertyView
import com.sibedge.yokodzun.android.utils.extensions.maxScoreUiString
import com.sibedge.yokodzun.android.utils.extensions.typeUiString
import com.sibedge.yokodzun.android.utils.extensions.variantsCountUiString
import com.sibedge.yokodzun.android.utils.managers.ColorManager


class TestTaskViewWrapper(
    context: Context,
    onClick: (TestTaskWithId) -> Unit,
    onMenuClick: (TestTaskWithId) -> Unit
) : PropertiesCell<TestTaskWithId>(
    context = context,
    onClick = onClick,
    activeColor = ColorManager.PRIMARY,
    inactiveColor = ColorManager.FG,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO,
    dataGetter = { task ->
        Data(
            title = StringGetter(R.string.test_info_layer_task_item_title, task.numberUiString),
            additionalActionButtonInfo = CellAdditionalActionButton.Info(
                icon = DrawableGetter(R.drawable.ic_options_primary),
                onClick = { onMenuClick(task) }
            )
        )
    },
    properties = listOf(
        PropertiesCellPropertyView.Data(
            name = StringGetter(R.string.test_info_layer_task_item_property_type),
            valueExtractor = { (_, testTask) ->
                testTask.typeUiString
            }
        ),
        PropertiesCellPropertyView.Data(
            name = StringGetter(R.string.test_info_layer_task_item_property_variants_count),
            valueExtractor = { (_, testTask) ->
                testTask.variantsCountUiString
            }
        ),
        PropertiesCellPropertyView.Data(
            name = StringGetter(R.string.test_info_layer_task_item_property_max_score),
            valueExtractor = { (_, testTask) ->
                testTask.maxScoreUiString
            }
        )
    )
)