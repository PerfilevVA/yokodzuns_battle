package com.sibedge.yokodzun.android.layers.section_info.list.item_view_wrapper

import android.content.Context
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerListCallback
import com.sibedge.yokodzun.android.layers.section_info.list.SectionInfoLayerListItem
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.properties.PropertiesCell
import com.sibedge.yokodzun.android.ui.cell.properties.PropertiesCellPropertyView
import com.sibedge.yokodzun.android.utils.extensions.passScorePercentageUiString
import com.sibedge.yokodzun.android.utils.extensions.timeLimitUiString
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import ru.hnau.remote_teaching_common.data.test.TestSkeleton


class SectionInfoLayerListTestViewWrapper(
    context: Context,
    sectionInfoLayerListCallback: SectionInfoLayerListCallback
) : PropertiesCell<SectionInfoLayerListItem>(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO,
    onClick = { item ->
        val test = item.value as TestSkeleton
        sectionInfoLayerListCallback.onTestClick(test)
    },
    dataGetter = { item ->
        val test = item.value as TestSkeleton
        PropertiesCell.Data(
            title = test.title.toGetter(),
            additionalActionButtonInfo = CellAdditionalActionButton.Info(
                icon = DrawableGetter(R.drawable.ic_options_primary),
                onClick = { sectionInfoLayerListCallback.onTestMenuClick(test) }
            )
        )
    },
    activeColor = ColorManager.PRIMARY,
    inactiveColor = ColorManager.FG,
    properties = listOf(
        PropertiesCellPropertyView.Data(
            name = StringGetter(R.string.section_info_layer_test_property_name_pass_score_percentage),
            valueExtractor = { item ->
                val test = item.value as TestSkeleton
                test.passScorePercentageUiString
            }
        ),
        PropertiesCellPropertyView.Data(
            name = StringGetter(R.string.section_info_layer_testtest_property_name_time_limit),
            valueExtractor = { item ->
                val test = item.value as TestSkeleton
                test.timeLimitUiString
            }
        )
    )
)