package com.sibedge.parameter.android.layers.admin.pages.parameter

import android.content.Context
import android.widget.FrameLayout
import com.sibedge.parameter.android.data.ParametersDataManager
import com.sibedge.parameter.android.ui.view.cell.ParameterView
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.view.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncViewsWithContentListContainer
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.Parameter
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.not


class AdminParametersPageView(
    context: Context,
    private val coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
) : FrameLayout(
    context
) {

    init {
        val list =
            AsyncViewsWithContentListContainer<Parameter>(
                context = context,
                idGetter = Parameter::id,
                invalidator = ParametersDataManager::invalidate,
                onEmptyListInfoViewGenerator = {
                    EmptyInfoView(
                        context = context,
                        text = StringGetter(R.string.admin_layer_parameters_page_no_parameters_title),
                        button = StringGetter(R.string.admin_layer_parameters_page_add_parameter) to this::onAddParameterClick
                    )
                },
                producer = ParametersDataManager as Producer<GetterAsync<Unit, List<Parameter>>>,
                viewWithDataGenerator = {
                    ParameterView(
                        context = context,
                        onClick = {
                            AdminParameterUtils.showParameterActions(
                                it,
                                coroutinesExecutor
                            )
                        }
                    )
                }
            )

        addChild(list)

        addPrimaryActionButton(
            icon = DrawableGetter(R.drawable.ic_add_fg),
            title = StringGetter(R.string.admin_layer_parameters_page_add_parameter),
            needShowTitle = list.onListScrolledToTopProducer.not(),
            onClick = this::onAddParameterClick
        ) {
            applyFrameParams {
                setMargins(SizeManager.DEFAULT_SEPARATION)
                setEndBottomGravity()
            }
        }
    }

    private fun onAddParameterClick() =
        coroutinesExecutor { ParametersDataManager.createNew() }

}