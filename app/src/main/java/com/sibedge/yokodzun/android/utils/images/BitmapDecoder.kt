package com.sibedge.yokodzun.android.utils.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import ru.hnau.jutils.ifTrue
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import kotlin.math.min


class BitmapDecoder(
    private val getter: SuspendGetter<String, ByteArray>
) : SuspendGetter<BitmapDecoder.Key, Bitmap> {

    data class Key(
        val url: String,
        val maxWidth: Int,
        val maxHeight: Int
    )

    override suspend fun get(key: Key): Bitmap {

        val (url, maxWidth, maxHeight) = key
        (maxWidth <= 0).ifTrue { throw IllegalArgumentException("maxWidth <= 0") }
        (maxHeight <= 0).ifTrue { throw IllegalArgumentException("maxHeight <= 0") }

        val bytes = getter.get(url)

        val (width, height) = getImageSize(bytes)
        if (width <= 0 || height <= 0) {
            throw RuntimeException("Image size is zero")
        }

        val options = BitmapFactory.Options()
            .apply { inSampleSize = resolveSampleSize(maxWidth, maxHeight, width, height) }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
    }

    private fun resolveSampleSize(
        maxWidth: Int, maxHeight: Int,
        width: Int, height: Int
    ): Int {
        (width < maxWidth || height < maxHeight).ifTrue { return 1 }
        val heightRatio = height.toFloat() / maxHeight.toFloat()
        val widthRatio = width.toFloat() / maxWidth.toFloat()
        return min(heightRatio, widthRatio).toInt()
    }

    private fun getImageSize(
        imageBytes: ByteArray
    ): Pair<Int, Int> {
        val options = BitmapFactory.Options()
            .apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size, options)
        return options.outWidth to options.outHeight
    }

}
