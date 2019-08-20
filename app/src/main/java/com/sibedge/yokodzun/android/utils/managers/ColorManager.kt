package com.sibedge.yokodzun.android.utils.managers

import android.content.Context
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.utils.ColorTriple
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.dp_px.dp2
import ru.hnau.androidutils.context_getters.dp_px.dp4
import ru.hnau.androidutils.context_getters.dp_px.dp8
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleDrawInfo
import ru.hnau.androidutils.ui.drawer.ripple.info.RippleInfo
import ru.hnau.androidutils.ui.drawer.shadow.info.ButtonShadowInfo
import ru.hnau.androidutils.ui.drawer.shadow.info.ShadowInfo
import ru.hnau.androidutils.ui.utils.Side
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.view_changer.ViewChangerInfo
import ru.hnau.androidutils.ui.view.view_presenter.PresentingViewProperties
import ru.hnau.androidutils.ui.view.waiter.material.MaterialWaiterView
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterColor
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterSize
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.producer.locked_producer.LockedProducer


object ColorManager {

    val TRANSPARENT = ColorGetter.TRANSPARENT

    val BG = ColorGetter.byResId(R.color.background)
    val BG_LIGHT = ColorGetter.byResId(R.color.background_light)
    val FG = ColorGetter.byResId(R.color.foreground)

    val PRIMARY = ColorGetter.byResId(R.color.primary)

    val PRIMARY_TRIPLE = ColorTriple(
        main = PRIMARY,
        dark = ColorGetter.byResId(R.color.primary_dark),
        light = ColorGetter.byResId(R.color.primary_light)
    )

    val GREEN_TRIPLE = ColorTriple(
        main = ColorGetter.byResId(R.color.green),
        dark = ColorGetter.byResId(R.color.green_dark),
        light = ColorGetter.byResId(R.color.green_light)
    )

    val ORANGE_TRIPLE = ColorTriple(
        main = ColorGetter.byResId(R.color.orange),
        dark = ColorGetter.byResId(R.color.orange_dark),
        light = ColorGetter.byResId(R.color.orange_light)
    )

    val RED_TRIPLE = ColorTriple(
        main = ColorGetter.byResId(R.color.red),
        dark = ColorGetter.byResId(R.color.red_dark),
        light = ColorGetter.byResId(R.color.red_light)
    )

    val PURPLE_TRIPLE = ColorTriple(
        main = ColorGetter.byResId(R.color.purple),
        dark = ColorGetter.byResId(R.color.purple_dark),
        light = ColorGetter.byResId(R.color.purple_light)
    )

    val GREY_TRIPLE = ColorTriple(
        main = ColorGetter.byResId(R.color.grey),
        dark = ColorGetter.byResId(R.color.grey_dark),
        light = ColorGetter.byResId(R.color.grey_light)
    )

    val FG_T50 = FG.mapWithAlpha(0.5f)
    val BG_T50 = BG.mapWithAlpha(0.5f)

    val RIPPLE_INFO = RippleInfo()

    const val RIPPLE_ALPHA = 0.25f

    val FG_ON_TRANSPARENT_RIPPLE_INFO = RippleDrawInfo(
        rippleInfo = RIPPLE_INFO,
        backgroundColor = TRANSPARENT,
        color = FG,
        rippleAlpha = RIPPLE_ALPHA
    )

    val PRIMARY_ON_TRANSPARENT_RIPPLE_INFO = RippleDrawInfo(
        rippleInfo = RIPPLE_INFO,
        backgroundColor = TRANSPARENT,
        color = PRIMARY,
        rippleAlpha = RIPPLE_ALPHA
    )

    val FG_LABEL_INFO = LabelInfo(
        fontType = FontManager.DEFAULT,
        textSize = SizeManager.TEXT_16,
        textColor = FG
    )

    val PRIMARY_LABEL_INFO = LabelInfo(
        fontType = FontManager.DEFAULT,
        textSize = SizeManager.TEXT_16,
        textColor = PRIMARY
    )

    val FG_SINGLE_LINE_LABEL_INFO =
        FG_LABEL_INFO.copy(
            maxLines = 1,
            minLines = 1
        )

    val PRIMARY_SINGLE_LINE_LABEL_INFO =
        PRIMARY_LABEL_INFO.copy(
            maxLines = 1,
            minLines = 1
        )

    val FG_SMALL_LABEL_INFO =
        FG_LABEL_INFO.copy(textSize = SizeManager.TEXT_12)

    val FG_SMALL_SINGLE_LINE_LABEL_INFO =
        FG_SINGLE_LINE_LABEL_INFO.copy(textSize = SizeManager.TEXT_12)

    val DEFAULT_SHADOW_INFO = ShadowInfo(dp4, dp8, ColorGetter.BLACK, 0.75f)
    val DEFAULT_PRESSED_SHADOW_INFO = ShadowInfo(dp2, dp4, ColorGetter.BLACK, 0.75f)
    val DEFAULT_BUTTON_SHADOW_INFO = ButtonShadowInfo(
        normal = DEFAULT_SHADOW_INFO,
        pressed = DEFAULT_PRESSED_SHADOW_INFO,
        animationTime = TimeValue.MILLISECOND * 100
    )

    val DEFAULT_SUSPEND_CACHED_GETTER_LOADER_INFO = ViewChangerInfo(
        fromSide = Side.BOTTOM,
        scrollFactor = 0.5f
    )

    val DEFAULT_PRESENTING_VIEW_PROPERTIES = PresentingViewProperties(
        fromSide = Side.BOTTOM,
        scrollFactor = 0.5f
    )

    val DEFAULT_WATER_COLOR = MaterialWaiterColor(
        foreground = PRIMARY,
        background = BG.mapWithAlpha(0.5f)
    )

    fun createWaiterView(
        context: Context,
        lockedProducer: LockedProducer,
        size: MaterialWaiterSize = MaterialWaiterSize.LARGE,
        color: MaterialWaiterColor = DEFAULT_WATER_COLOR
    ) =
        MaterialWaiterView(
            context = context,
            lockedProducer = lockedProducer,
            size = size,
            color = color,
            visibilitySwitchingTime = TimeValue.MILLISECOND * 500
        )

}