package com.sibedge.yokodzun.android.utils.images

import android.graphics.Bitmap
import okhttp3.OkHttpClient
import ru.hnau.androidutils.utils.ContextConnector
import ru.hnau.jutils.cache.AutoCache


object ImagesLoader {

    private const val CACHE_SIZE_USE_PERCENTAGE = 0.1f

    private val bitmapDecoder = BitmapDecoder(
        getter = FilesLoader(
            client = OkHttpClient.Builder().apply {
                val context = ContextConnector.context
                DiskCacheLocation.findMax(context)?.toCache(CACHE_SIZE_USE_PERCENTAGE)?.let(this::cache)
            }.build()
        )
    )

    private val bitmapsCache = AutoCache<BitmapDecoder.Key, CachingSuspendLambda<Bitmap>>(
        capacity = 128,
        getter = { key -> CachingSuspendLambda { bitmapDecoder.get(key) } }
    )

    suspend fun get(url: String, maxWidth: Int, maxHeight: Int) =
        bitmapsCache.get(BitmapDecoder.Key(url, maxWidth, maxHeight)).get()

}