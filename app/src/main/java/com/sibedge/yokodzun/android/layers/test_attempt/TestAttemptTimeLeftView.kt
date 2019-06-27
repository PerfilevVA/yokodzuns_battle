package com.sibedge.yokodzun.android.layers.test_attempt

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.label.addLabel
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.utils.handler.HandlerMetronome
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.takeIfNotEmpty
import com.sibedge.yokodzun.android.ui.PercentageView
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class TestAttemptTimeLeftView(
    context: Context,
    timeLeft: TimeValue
) : LinearLayout(context) {

    companion object {

        private val LABEL_INFO = LabelInfo(
            maxLines = 1,
            minLines = 1,
            gravity = HGravity.CENTER,
            textSize = SizeManager.TEXT_16,
            fontType = FontManager.DEFAULT,
            textColor = ColorManager.PRIMARY
        )

    }

    private val start = TimeValue.now()
    private val finish = start + timeLeft

    private val metronome = HandlerMetronome(
        initialPeriod = TimeValue.SECOND
    )

    init {
        applyHorizontalOrientation()
        applyHorizontalPadding(SizeManager.DEFAULT_SEPARATION)

        addChild(
            PercentageView(
                context = context,
                progress = metronome.map { ((TimeValue.now() - start) / (finish - start)).toFloat() },
                color = ColorManager.BAD
            )
        ) {
            applyLinearParams {
                setStretchedWidth(4f)
            }
        }

        addLabel(
            info = LABEL_INFO
        ) {
            applyLinearParams {
                setStretchedWidth(1f)
            }
            metronome.attach {
                val timeLeftLocal = finish - TimeValue.now()
                text = timeLeftLocal.toTimeLeftString.toGetter()
            }
        }
    }

    private val TimeValue.toTimeLeftString: String
        get() = listOf(
            daysDigits to "д",
            hoursDigits to "ч",
            minutesDigits to "м",
            secondsDigits to "с"
        )
            .dropWhile { it.first <= 0 }
            .joinToString(
                separator = " ",
                transform = { "${it.first}${it.second}" }
            )
            .takeIfNotEmpty()
            ?: "0с"

}