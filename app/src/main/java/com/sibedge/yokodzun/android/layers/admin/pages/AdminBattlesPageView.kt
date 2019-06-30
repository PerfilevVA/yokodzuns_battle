package com.sibedge.yokodzun.android.layers.admin.pages

import android.content.Context
import android.widget.FrameLayout
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.admin.AdminAllBattlesDataManager
import com.sibedge.yokodzun.android.ui.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.cell.AdminBattleView
import com.sibedge.yokodzun.android.ui.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.list.base.ViewsWithContentListContainer
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import kotlinx.coroutines.CoroutineScope
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.not


class AdminBattlesPageView(
    context: Context,
    private val coroutinesExecutor: (suspend CoroutineScope.() -> Unit) -> Unit
) : FrameLayout(
    context
) {

    init {
        val list = ViewsWithContentListContainer<Battle>(
            context = context,
            idGetter = Battle::id,
            invalidator = AdminAllBattlesDataManager::invalidate,
            onEmptyListInfoViewGenerator = {
                EmptyInfoView(
                    context = context,
                    text = StringGetter(R.string.admin_add_layer_battles_page_no_battles_title),
                    button = StringGetter(R.string.admin_add_layer_battles_add_battle) to this::onAddBattleClick
                )
            },
            producer = AdminAllBattlesDataManager as Producer<GetterAsync<Unit, List<Battle>>>,
            viewWithContentGenerator = { AdminBattleView(context, coroutinesExecutor) }
        )

        addChild(list)

        addPrimaryActionButton(
            icon = DrawableGetter(R.drawable.ic_add_white),
            title = StringGetter(R.string.admin_add_layer_battles_add_battle),
            needShowTitle = list.onListScrolledToTopProducer.not(),
            onClick = this::onAddBattleClick
        ) {
            applyFrameParams {
                setMargins(SizeManager.DEFAULT_SEPARATION)
                setEndBottomGravity()
            }
        }
    }

    private fun onAddBattleClick() =
        coroutinesExecutor { AdminAllBattlesDataManager.createNew() }


}