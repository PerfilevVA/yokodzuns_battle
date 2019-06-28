package com.sibedge.yokodzun.android.ui.button.primary

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.drawables.layout_drawable.view.addLayoutDrawableView
import ru.hnau.androidutils.ui.utils.Side
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.apply.applyCenterGravity
import ru.hnau.androidutils.ui.view.utils.apply.applyEndPadding
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLayoutParams
import ru.hnau.androidutils.ui.view.view_presenter.*
import ru.hnau.jutils.producer.Producer
import ru.hnau.jutils.producer.extensions.map
import ru.hnau.jutils.producer.extensions.toProducer


class PrimaryActivityButtonContent(
        context: Context,
        icon: DrawableGetter,
        title: StringGetter,
        needShowTitle: Producer<Boolean> = true.toProducer(),
        private val info: PrimaryActionButtonInfo = PrimaryActionButtonInfo.DEFAULT
) : LinearLayout(
        context
) {

    private val presentingTitleInfo = getPresentingTitleInfo(title)
    private val presentingEmptyInfo = getPresentingTitleInfo(null)

    init {
        applyHorizontalOrientation()
        applyCenterGravity()
        addLayoutDrawableView(
                content = icon
        ) {
            applyLayoutParams {
                setSize(info.minSize)
            }
        }

        addPresenter(
                presentingViewProducer = needShowTitle.map(
                        forTrue = presentingTitleInfo,
                        forFalse = presentingEmptyInfo
                ),
                presenterViewInfo = PresenterViewInfo(
                        verticalSizeInterpolator = SizeInterpolator.MAX
                )
        )
    }

    private fun getPresentingTitleInfo(
            title: StringGetter?
    ) =
            PresentingViewInfo(
                    view = title?.let { content ->
                        Label(
                                context = context,
                                info = info.titleLabelInfo,
                                initialText = content
                        ).applyEndPadding(info.minSize / 2)
                    },
                    properties = PresentingViewProperties(
                            animatingTime = info.animatingTime,
                            fromSide = Side.END,
                            toSide = Side.END,
                            gravity = HGravity.START_CENTER_VERTICAL
                    )
            )

}