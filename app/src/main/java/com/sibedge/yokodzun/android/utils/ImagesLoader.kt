package com.sibedge.yokodzun.android.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


object ImagesLoader {

    suspend fun load(
        url: String
    ) = suspendCoroutine<Bitmap> { continuation ->
        Picasso
            .get()
            .load(Uri.parse(url))
            .into(
                object : Target {

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                    override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) =
                        continuation.resumeWithException(e)

                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) =
                        continuation.resume(bitmap)

                }

            )
    }

}