package com.sibedge.yokodzun.android.layers.rater

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.RaterBattleDataManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.view.AsyncImageView
import com.sibedge.yokodzun.android.ui.view.addSuspendPresenter
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.utils.ScreenManager
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.utils.createIsVisibleToUserProducer
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterSize
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.getter.base.get
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.observeWhen


class RaterLayer(
    context: Context
) : AppLayer(
    context = context,
    showGoBackButton = false
) {

    override var title = StringGetter(R.string.app_name)
        set(value) {
            field = value
            updateTitle()
        }

    private val battleLogoView = AsyncImageView(
        context = context,
        waiterSize = MaterialWaiterSize.LARGE
    ).applyLinearParams {
        setSize(ScreenManager.width)
    }

    override fun afterCreate() {
        super.afterCreate()

        content {

            addSuspendPresenter(
                producer = RaterBattleDataManager as Producer<GetterAsync<Unit, Battle>>,
                invalidator = RaterBattleDataManager::invalidate,
                contentViewGenerator = { battle ->
                    title = battle.entityNameWithTitle
                    RaterLayerContentView(context, battle)
                }
            ) {
                applyLinearParams {
                    setStretchedHeight()
                    setMatchParentWidth()
                }
            }

        }

    }

}