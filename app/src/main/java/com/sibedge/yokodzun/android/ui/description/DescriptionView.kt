package com.sibedge.yokodzun.android.ui.description

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.AsyncImageView
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.circle.CircleBitmapDrawable
import com.sibedge.yokodzun.android.ui.circle.CircleLetterDrawable
import com.sibedge.yokodzun.android.ui.circle.CircleLogoDrawable
import com.sibedge.yokodzun.android.utils.ImagesLoader
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager
import com.sibedge.yokodzun.android.utils.tryOrLogToCrashlitics
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.dp64
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.apply.addVerticalLayout
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterSize
import ru.hnau.jutils.takeIfNotEmpty


class DescriptionView(
    context: Context
) : LinearLayout(
    context
), ViewWithContent<DescriptionView.Item> {

    data class Item(
        val description: Description,
        val mainColor: ColorGetter = ColorManager.PRIMARY
    )

    override val view = this

    override var content: Item? = null
        set(value) {
            field = value
            updateContent(value)
        }

    private val logoView = DescriptionLogoView(
        context = context
    ).apply {
        applyLinearParams {
            setSize(dp64)
            setTopGravity()
        }
    }

    private val titleView = Label(
        context = context,
        textColor = ColorManager.PRIMARY,
        fontType = FontManager.DEFAULT,
        textSize = SizeManager.TEXT_16,
        gravity = HGravity.START_CENTER_VERTICAL,
        maxLines = 3
    )

    private val subtitleView = Label(
        context = context,
        textColor = ColorManager.FG,
        fontType = FontManager.DEFAULT,
        textSize = SizeManager.TEXT_12,
        gravity = HGravity.START_CENTER_VERTICAL,
        maxLines = 8
    ).apply {
        applyLinearParams {
            setMatchParentWidth()
            setTopMargin(SizeManager.SMALL_SEPARATION)
        }
    }

    init {
        applyHorizontalOrientation()
        addView(logoView)
        addVerticalLayout {
            applyLinearParams {
                setStretchedWidth()
                setStartMargin(SizeManager.DEFAULT_SEPARATION)
            }
            addView(titleView)
            addView(subtitleView)
        }
    }

    private fun updateContent(item: Item?) {

        val description = item?.description

        logoView.content = description

        titleView.textColor = item?.mainColor ?: ColorManager.PRIMARY
        titleView.text = description?.title?.takeIfNotEmpty()?.toGetter()
            ?: StringGetter(R.string.description_view_no_title_placeholder)

        subtitleView.text = description?.description?.takeIfNotEmpty()?.toGetter()
            ?: StringGetter(R.string.description_view_no_description_placeholder)

    }

}
