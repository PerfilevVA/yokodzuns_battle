package com.sibedge.yokodzun.android.layers.test_attempt

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.Side
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.label.LabelInfo
import ru.hnau.androidutils.ui.view.utils.apply.*
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.view_presenter.*
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.int.div
import com.sibedge.yokodzun.android.ui.PercentageView
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class TestAttemptProgressView(
    context: Context,
    progress: Producer<Int>,
    max: Int
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


        private val PRESENTING_VIEW_PROPERTIES = PresentingViewProperties(
            fromSide = Side.BOTTOM,
            scrollFactor = 0.5f,
            animatingTime = TimeValue.MILLISECOND * 200
        )

    }

    init {
        applyHorizontalOrientation()
        applyHorizontalPadding(SizeManager.DEFAULT_SEPARATION)

        addChild(
            PercentageView(
                context = context,
                progress = progress / max.toFloat(),
                color = ColorManager.PRIMARY
            )
        ) {
            applyLinearParams {
                setStretchedWidth(4f)
            }
        }

        addHorizontalLayout {

            applyCenterGravity()

            applyLinearParams {
                setStretchedWidth(1f)
            }

            addChild(
                PresenterView(
                    context = context,
                    presenterViewInfo = PresenterViewInfo(
                        verticalSizeInterpolator = SizeInterpolator.MAX
                    ),
                    presentingViewProducer = progress.map { progressCount ->
                        Label(
                            context = context,
                            info = LABEL_INFO,
                            initialText = progressCount.toString().toGetter()
                        ).toPresentingInfo(PRESENTING_VIEW_PROPERTIES)
                    }
                )
            ) {
                applyLinearParams {
                    setEndMargin(SizeManager.EXTRA_SMALL_SEPARATION)
                }
            }

            addChild(
                Label(
                    context = context,
                    info = LABEL_INFO,
                    initialText = "/ $max".toGetter()
                )
            )

        }
    }

}