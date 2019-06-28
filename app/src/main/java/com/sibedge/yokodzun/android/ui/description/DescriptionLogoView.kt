package com.sibedge.yokodzun.android.ui.description

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import com.sibedge.yokodzun.android.ui.AsyncImageView
import com.sibedge.yokodzun.android.ui.ViewWithContent
import com.sibedge.yokodzun.android.ui.circle.CircleBitmapDrawable
import com.sibedge.yokodzun.android.ui.circle.CircleLetterDrawable
import com.sibedge.yokodzun.android.ui.circle.CircleLogoDrawable
import com.sibedge.yokodzun.android.utils.ImagesLoader
import com.sibedge.yokodzun.android.utils.tryOrLogToCrashlitics
import com.sibedge.yokodzun.common.data.helpers.Description
import ru.hnau.androidutils.context_getters.dp_px.dp64
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.view.utils.getDefaultMeasurement
import ru.hnau.androidutils.ui.view.utils.horizontalPaddingSum
import ru.hnau.androidutils.ui.view.utils.makeExactlyMeasureSpec
import ru.hnau.androidutils.ui.view.utils.verticalPaddingSum
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterColor
import ru.hnau.androidutils.ui.view.waiter.material.drawer.params.MaterialWaiterSize
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.coroutines.delay
import ru.hnau.jutils.takeIfNotEmpty


class DescriptionLogoView(
    context: Context
) : AsyncImageView(
    context = context,
    waiterSize = MaterialWaiterSize.SMALL
), ViewWithContent<Description> {

    companion object {

        private val PREFERRED_SIZE = dp64

    }

    override val view = this

    override var content: Description? = null
        set(value) {
            field = value
            setImage { loadLogo(value).toGetter() }
        }

    private suspend fun loadLogo(description: Description?) =
        loadCircleDrawableByUrl(description?.logoUrl)
            ?: description?.title?.takeIfNotEmpty()?.let { CircleLetterDrawable(context, it) }
            ?: CircleLogoDrawable(context)

    private suspend fun loadCircleDrawableByUrl(url: String?): Drawable? {
        val bitmap = loadByUrl(url) ?: return null
        return CircleBitmapDrawable(bitmap)
    }

    private suspend fun loadByUrl(url: String?): Bitmap? {
        url?.takeIfNotEmpty() ?: return null
        return tryOrLogToCrashlitics {
            ImagesLoader.load(url)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            makeExactlyMeasureSpec(
                getDefaultMeasurement(
                    widthMeasureSpec,
                    PREFERRED_SIZE.getPxInt(context) + horizontalPaddingSum
                )
            ),
            makeExactlyMeasureSpec(
                getDefaultMeasurement(
                    heightMeasureSpec,
                    PREFERRED_SIZE.getPxInt(context) + verticalPaddingSum
                )
            )
        )
    }

}