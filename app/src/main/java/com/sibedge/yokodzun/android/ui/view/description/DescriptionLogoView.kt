package com.sibedge.yokodzun.android.ui.view.description

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.sibedge.yokodzun.android.ui.view.AsyncImageView
import com.sibedge.yokodzun.android.ui.ViewWithData
import com.sibedge.yokodzun.android.ui.view.circle.CircleBitmapDrawable
import com.sibedge.yokodzun.android.ui.view.circle.CircleLetterDrawable
import com.sibedge.yokodzun.android.ui.view.circle.CircleLogoDrawable
import com.sibedge.yokodzun.android.utils.images.ImagesLoader
import com.sibedge.yokodzun.android.utils.tryOrLogToCrashlitics
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter
import ru.hnau.androidutils.context_getters.dp_px.dp64
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.utils.getDefaultMeasurement
import ru.hnau.androidutils.ui.view.utils.horizontalPaddingSum
import ru.hnau.androidutils.ui.view.utils.makeExactlyMeasureSpec
import ru.hnau.androidutils.ui.view.utils.verticalPaddingSum
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterSize
import ru.hnau.jutils.takeIfNotEmpty


class DescriptionLogoView(
    context: Context,
    preferredSize: DpPxGetter = PREFERRED_SIZE
) : AsyncImageView(
    context = context,
    waiterSize = MaterialWaiterSize.SMALL
), ViewWithData<Description> {

    companion object {

        private val PREFERRED_SIZE = dp64

    }

    private val preferredSize = preferredSize.getPxInt(context)

    override val view = this

    override var data: Description? = null
        set(value) {
            field = value
            setImage { loadLogo(value).toGetter() }
        }

    private suspend fun loadLogo(description: Description?) =
        loadCircleDrawableByUrl(description?.logoUrl)
            ?: description?.title?.takeIfNotEmpty()?.let {
                CircleLetterDrawable(
                    context,
                    it
                )
            }
            ?: CircleLogoDrawable(context)

    private suspend fun loadCircleDrawableByUrl(url: String?): Drawable? {
        val bitmap = loadByUrl(url) ?: return null
        return CircleBitmapDrawable(bitmap)
    }

    private suspend fun loadByUrl(url: String?): Bitmap? {
        url?.takeIfNotEmpty() ?: return null
        return tryOrLogToCrashlitics {
            ImagesLoader.get(url, preferredSize, preferredSize)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            makeExactlyMeasureSpec(getDefaultMeasurement(widthMeasureSpec, preferredSize + horizontalPaddingSum)),
            makeExactlyMeasureSpec(getDefaultMeasurement(heightMeasureSpec, preferredSize + verticalPaddingSum))
        )
    }

}