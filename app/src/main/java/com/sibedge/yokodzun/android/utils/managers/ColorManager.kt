package com.sibedge.yokodzun.android.utils.managers

import android.content.Context
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.dp_px.*
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
import com.sibedge.yokodzun.android.R


object ColorManager {

    val TRANSPARENT = ColorGetter.TRANSPARENT
    val WHITE = ColorGetter.byResId(R.color.white)
    val BLACK = ColorGetter.byResId(R.color.black)
    val PRIMARY_DEFAULT = ColorGetter.byResId(R.color.primary)
    val BAD_DEFAULT = ColorGetter.byResId(R.color.bad)
    val GOOD_DEFAULT = ColorGetter.byResId(R.color.good)

    val BG = WHITE
    val FG = BLACK
    val PRIMARY = PRIMARY_DEFAULT
    val BAD = BAD_DEFAULT
    val GOOD = GOOD_DEFAULT

    val FG_T50 = FG.mapWithAlpha(0.5f)
    val BG_T50 = BG.mapWithAlpha(0.5f)

    val RIPPLE_INFO = RippleInfo()

    private const val RIPPLE_ALPHA = 0.1f

    val BG_ON_PRIMARY_RIPPLE_INFO = RippleDrawInfo(
        rippleInfo = RIPPLE_INFO,
        backgroundColor = PRIMARY,
        color = BG,
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

    val DEFAULT_SHADOW_INFO = ShadowInfo(dp4, dp8, BLACK, 0.4f)
    val DEFAULT_PRESSED_SHADOW_INFO = ShadowInfo(dp2, dp4, BLACK, 0.4f)
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

    fun createWaiterView(
        context: Context,
        lockedProducer: LockedProducer,
        size: MaterialWaiterSize = MaterialWaiterSize.LARGE
    ) =
        MaterialWaiterView(
            context = context,
            lockedProducer = lockedProducer,
            size = size,
            color = MaterialWaiterColor(
                foreground = PRIMARY,
                background = WHITE.mapWithAlpha(0.5f)
            ),
            visibilitySwitchingTime = TimeValue.MILLISECOND * 500
        )

}