package com.sibedge.yokodzun.android.layers.raters

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.RatersDataManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.view.button.primary.PrimaryActionButtonInfo
import com.sibedge.yokodzun.android.ui.view.button.primary.addPrimaryActionButton
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncItemsListContaner
import com.sibedge.yokodzun.android.ui.view.plus_minus.PlusMinusColumnInfo
import com.sibedge.yokodzun.android.utils.FileUtils
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import com.sibedge.yokodzun.common.utils.Validators
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addFrameLayout
import ru.hnau.androidutils.ui.view.utils.apply.addVerticalLayout
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyFrameParams
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.utils.shortToast
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.getter.base.get
import ru.hnau.jutils.me
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.toProducer


class BattleRatersLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        fun newInstance(
            context: Context,
            battle: Battle
        ) = BattleRatersLayer(context).apply {
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
                    button = StringGetter(R.string.raters_layer_no_raters_add_parameter) to this@BattleRatersLayer::addRaters
                )
            },
            invalidator = ratersDataManager::invalidate,
            idGetter = String::me,
            viewWrappersCreator = {
                RaterView(
                    context = context,
                    onRemoveClick = this@BattleRatersLayer::askAndRemoveRater
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

                addVerticalLayout {

                    applyFrameParams {
                        setMargins(SizeManager.DEFAULT_SEPARATION)
                        setEndBottomGravity()
                    }

                    addPrimaryActionButton(
                        icon = DrawableGetter(R.drawable.ic_send_fg),
                        title = StringGetter(""),
                        needShowTitle = false.toProducer(),
                        onClick = this@BattleRatersLayer::sendRatersCodes,
                        info = PrimaryActionButtonInfo(
                            rippleDrawInfo = RippleDrawInfo(
                                backgroundColor = ColorGetter.byResId(R.color.orange)
                            )
                        )
                    ) {
                        applyLinearParams {
                            setBottomMargin(SizeManager.SMALL_SPACE)
                        }
                    }

                    addPrimaryActionButton(
                        icon = DrawableGetter(R.drawable.ic_add_fg),
                        title = StringGetter(""),
                        needShowTitle = false.toProducer(),
                        onClick = this@BattleRatersLayer::addRaters
                    )
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
                title = "10".toGetter(),
                actionMinus = { it - 10 },
                actionPlus = { it + 10 }
            ),
            PlusMinusColumnInfo(
                title = "1".toGetter(),
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

    private fun sendRatersCodes() {
        ratersDataManager.attach { ratersCodesGetter ->
            uiJobLocked {
                val ratersCodes = ratersCodesGetter.get().ifEmpty {
                    shortToast(StringGetter(R.string.raters_layer_no_raters_cant_send_toast))
                    return@uiJobLocked
                }
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    val subject = context.resources.getString(R.string.send_raters_codes_intent_subject)
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    type = "text/csv"
                    val fileName = context.resources.getString(R.string.send_raters_codes_intent_file_name)
                    val file = FileUtils.extractListToFile(context.filesDir, fileName, ratersCodes)
                    val fileUri = FileProvider.getUriForFile(context, context.packageName, file!!)
                    putExtra(Intent.EXTRA_STREAM, fileUri)
                }
                val chooser = context.resources.getString(R.string.send_raters_codes_intent_chooser)
                context.startActivity(Intent.createChooser(sendIntent, chooser))
            }
        }.detach()
    }

}