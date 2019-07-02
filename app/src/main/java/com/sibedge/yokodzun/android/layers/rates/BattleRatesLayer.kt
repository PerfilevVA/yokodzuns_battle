package com.sibedge.yokodzun.android.layers.rates

import android.content.Context
import android.view.ViewGroup
import com.sibedge.parameter.android.data.ParametersDataManager
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.data.BattleRatesDataManager
import com.sibedge.yokodzun.android.data.YokodzunsDataManager
import com.sibedge.yokodzun.android.layers.base.AppLayer
import com.sibedge.yokodzun.android.ui.view.addSuspendPresenter
import com.sibedge.yokodzun.android.ui.view.empty_info.EmptyInfoView
import com.sibedge.yokodzun.android.ui.view.list.base.async.AsyncItemsListContaner
import com.sibedge.yokodzun.android.utils.RateCalculator
import com.sibedge.yokodzun.android.utils.Utils
import com.sibedge.yokodzun.android.utils.extensions.entityNameWithTitle
import com.sibedge.yokodzun.android.utils.managers.AppActivityConnector
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter.Companion.dp
import ru.hnau.androidutils.ui.drawables.layout_drawable.view.LayoutDrawableView
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.clickable.ClickableLinearLayout
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.layer.layer.LayerState
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.applyStartPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.getter.SuspendGetter
import ru.hnau.jutils.getter.base.GetterAsync
import ru.hnau.jutils.getter.base.get
import ru.hnau.jutils.getter.base.map
import ru.hnau.jutils.getter.toGetter
import ru.hnau.jutils.producer.ActualProducerSimple
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.combine


class BattleRatesLayer(
    context: Context
) : AppLayer(
    context = context
) {

    companion object {

        private val ALL_PARAMETERS = StringGetter(R.string.battle_rates_layer_all_parameters)

        fun newInstance(
            context: Context,
            battle: Battle
        ) = BattleRatesLayer(context).apply {
            this.battle = battle
        }

    }

    @LayerState
    private lateinit var battle: Battle

    override val title get() = battle.entityNameWithTitle

    private val selectedParameterProducer = ActualProducerSimple<String?>(null)

    private val battleRatesDataManager
            by lazy { BattleRatesDataManager[battle.id] }

    private val yokodzunsRateResults by lazy {
        Producer.combine(
            producer1 = YokodzunsDataManager,
            producer2 = battleRatesDataManager,
            producer3 = selectedParameterProducer
        ) { yokodzunsGetter, ratesGetter, selectedParameter ->
            SuspendGetter.simple {
                val yokodzuns = yokodzunsGetter.get()
                val rates = ratesGetter.get()
                RateCalculator.calcRate(
                    battle = battle,
                    parameterId = selectedParameter,
                    battleRates = rates,
                    yokodzuns = yokodzuns
                )
            }
        }
    }

    private val selectedParameterNameProducer = Producer.combine(
        producer1 = ParametersDataManager,
        producer2 = selectedParameterProducer
    ) { parametersGetter, selectedParameterId ->
        if (selectedParameterId == null) {
            return@combine SuspendGetter.simple(ALL_PARAMETERS)
        }
        parametersGetter.map { parameters ->
            val parameter = parameters.find { it.id == selectedParameterId }
                ?: return@map ALL_PARAMETERS
            return@map StringGetter(parameter.description.title)
        }
    }

    private val selectedParameterNameView = Label(
        context = context,
        textColor = ColorManager.FG,
        textSize = SizeManager.TEXT_16,
        gravity = HGravity.CENTER,
        minLines = 1,
        maxLines = 1,
        fontType = FontManager.BOLD
    ).applyLinearParams {
        setStretchedWidth()
    }

    private val selectedParameterView = ClickableLinearLayout(
        context = context,
        rippleDrawInfo = ColorManager.FG_ON_TRANSPARENT_RIPPLE_INFO,
        onClick = this::selectParameter
    ).apply {
        applyLinearParams {
            setMatchParentWidth()
            setHeight(Utils.HEADER_HEIGHT)
        }
        applyHorizontalOrientation()
        applyCenterGravity()
        applyStartPadding(Utils.HEADER_HEIGHT)
        addChild(selectedParameterNameView)
        addChild(
            LayoutDrawableView(
                context = context,
                initialContent = DrawableGetter(R.drawable.ic_select_fg)
            ).applyLinearParams {
                setSize(Utils.HEADER_HEIGHT)
            }
        )
    }

    override fun afterCreate() {
        super.afterCreate()

        topContent {

            addSuspendPresenter<StringGetter, ViewGroup>(
                producer = selectedParameterNameProducer,
                invalidator = ParametersDataManager::invalidate,
                contentViewGenerator = { parameterName ->
                    selectedParameterNameView.text = parameterName
                    selectedParameterView
                }
            )

        }

        content {

            addView(
                AsyncItemsListContaner(
                    context = context,
                    invalidator = {
                        battleRatesDataManager.invalidate()
                        YokodzunsDataManager.invalidate()
                    },
                    producer = yokodzunsRateResults as Producer<GetterAsync<Unit, List<RateCalculator.Value>>>,
                    idGetter = { it.yokodzun.id },
                    viewWrappersCreator = { YokodzunRateView(context) },
                    onEmptyListInfoViewGenerator = {
                        EmptyInfoView(
                            context = context,
                            text = StringGetter(R.string.battle_rates_layer_no_results)
                        )
                    }
                )
                    .applyLinearParams {
                        setStretchedHeight()
                        setMatchParentWidth()
                    }
            )

        }

    }

    private fun selectParameter() {
        uiJobLocked {
            val parameters = ParametersDataManager.wait().get().filter { parameter ->
                battle.parameters.any { battleParamater ->
                    parameter.id == battleParamater.id
                }
            }
            AppActivityConnector.showBottomSheet {
                title(StringGetter(R.string.battle_rates_layer_select_parameter_title))
                text(StringGetter(R.string.battle_rates_layer_select_parameter_text))
                closeItem(ALL_PARAMETERS) {
                    selectedParameterProducer.updateState(null)
                }
                parameters.forEach { parameter ->
                    closeItem(StringGetter(parameter.description.title)) {
                        selectedParameterProducer.updateState(parameter.id)
                    }
                }
            }
        }
    }


}