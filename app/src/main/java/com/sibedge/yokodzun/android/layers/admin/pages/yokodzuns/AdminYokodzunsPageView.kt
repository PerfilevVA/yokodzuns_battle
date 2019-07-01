package com.sibedge.yokodzun.android.layers.admin.pages.yokodzuns

import android.content.Context
import android.widget.FrameLayout
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.YokodzunsDataManager
import com.sibedge.yokodzun.android.ui.view.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.view.cell.YokodzunView
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.ViewsWithContentListContainer
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.Yokodzun
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.not


class AdminYokodzunsPageView(
    context: Context,
    private val coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
) : FrameLayout(
    context
) {

    init {
        val list =
            ViewsWithContentListContainer<Yokodzun>(
                context = context,
                idGetter = Yokodzun::id,
                invalidator = YokodzunsDataManager::invalidate,
                onEmptyListInfoViewGenerator = {
                    EmptyInfoView(
                        context = context,
                        text = StringGetter(R.string.admin_layer_yokodzuns_page_no_yokodzuns_title),
                        button = StringGetter(R.string.admin_layer_yokodzuns_page_add_yokodzun) to this::onAddYokodzunClick
                    )
                },
                producer = YokodzunsDataManager as Producer<GetterAsync<Unit, List<Yokodzun>>>,
                viewWithContentGenerator = {
                    YokodzunView(
                        context = context,
                        onClick = { AdminYokodzunUtils.showYokodzunActions(it, coroutinesExecutor) }
                    )
                }
            )

        addChild(list)

        addPrimaryActionButton(
            icon = DrawableGetter(R.drawable.ic_add_white),
            title = StringGetter(R.string.admin_layer_yokodzuns_page_add_yokodzun),
            needShowTitle = list.onListScrolledToTopProducer.not(),
            onClick = this::onAddYokodzunClick
        ) {
            applyFrameParams {
                setMargins(SizeManager.DEFAULT_SEPARATION)
                setEndBottomGravity()
            }
        }
    }

    private fun onAddYokodzunClick() =
        coroutinesExecutor { YokodzunsDataManager.createNew() }

}