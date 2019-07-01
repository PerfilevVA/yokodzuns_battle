package com.sibedge.yokodzun.android.layers.raters

import android.content.Context
import androidx.lifecycle.Transformations.map
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.RatersDataManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.layers.battle.parameters.item.BattleFullParameter
import com.sibedge.yokodzun.android.layers.battle.parameters.item.BattleFullParameterView
import com.sibedge.yokodzun.android.layers.description.EditBattleDescriptionLayer
import com.sibedge.yokodzun.android.ui.view.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncItemsListContaner
import com.sibedge.yokodzun.android.ui.view.plus_minus.PlusMinusColumnInfo
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.utils.Validators
import kotlinx.coroutines.cancel
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.handle
import ru.hnau.jutils.me
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.not
import java.util.Locale.filter


class RatersLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = RatersLayer(context).apply {
            this.battle = battle
        }

    }

    @LayerState
    private lateinit var battle: Battle

    override val title
        get() = StringGetter(R.string.raters_layer_title, battle.description.title)

    private val ratersDataManager
        get() = RatersDataManager[battle.id]

    override fun afterCreate() {
        super.afterCreate()

        val list = AsyncItemsListContaner<String>(
            context = context,
            producer = ratersDataManager as Producer<GetterAsync<Unit, List<String>>>,
            onEmptyListInfoViewGenerator = {
                EmptyInfoView(
                    context = context,
                    text = StringGetter(R.string.raters_layer_no_raters_title),
                    button = StringGetter(R.string.raters_layer_no_raters_add_parameter) to this@RatersLayer::addRaters
                )
            },
            invalidator = ratersDataManager::invalidate,
            idGetter = String::me,
            viewWrappersCreator = {
                RaterView(
                    context = context,
                    onRemoveClick = this@RatersLayer::askAndRemoveRater
                )
            }
        )

        content {
            addFrameLayout {

                applyLinearParams {
                    setStretchedHeight()
                    setMatchParentWidth()
                }

                addView(list)

                addPrimaryActionButton(
                    icon = DrawableGetter(R.drawable.ic_add_fg),
                    title = StringGetter(R.string.raters_layer_no_raters_add_parameter),
                    needShowTitle = list.onListScrolledToTopProducer.not(),
                    onClick = this@RatersLayer::addRaters
                ) {
                    applyFrameParams {
                        setMargins(SizeManager.DEFAULT_SEPARATION)
                        setEndBottomGravity()
                    }
                }


            }

        }
    }

    private fun addRaters() = AppActivityConnector.showPlusMinusDialog(
        title = StringGetter(R.string.raters_layer_add_count_title),
        text = StringGetter(R.string.raters_layer_add_count_text),
        confirmButtonText = StringGetter(R.string.dialog_save),
        initialValue = 10,
        valueToStringConverter = { it.toString().toGetter() },
        columns = listOf(
            PlusMinusColumnInfo(
                title = "10".toString().toGetter(),
                actionMinus = { it - 10 },
                actionPlus = { it + 10 }
            ),
            PlusMinusColumnInfo(
                title = "1".toString().toGetter(),
                actionMinus = { it - 1 },
                actionPlus = { it + 1 }
            )
        ),
        availableValueRange = Validators.CREATE_BATTLES_RATERS_MIN_COUNT..Validators.CREATE_BATTLES_RATERS_MAX_COUNT
    ) { count ->
        uiJobLocked { ratersDataManager.add(count) }
        return@showPlusMinusDialog true
    }

    private fun askAndRemoveRater(
        code: String
    ) = AppActivityConnector.showConfirmDialog(
        title = StringGetter(R.string.raters_layer_remove_confirm_dialog_title),
        text = StringGetter(R.string.raters_layer_remove_confirm_dialog_text),
        confirmText = StringGetter(R.string.dialog_remove)
    ) {
        uiJobLocked { ratersDataManager.remove(code) }
    }

}