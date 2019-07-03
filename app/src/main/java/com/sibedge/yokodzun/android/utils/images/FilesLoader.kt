package com.sibedge.yokodzun.android.utils.images

import okhttp3.*
import ru.hnau.jutils.handle
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class FilesLoader(
    private val client: OkHttpClient
) : SuspendGetter<String, ByteArray> {

    /*private val client =
        OkHttpClient.Builder()
            .cache(Cache())
            .build()*/

    override suspend fun get(
        key: String
    ): ByteArray {

        val request = Request.Builder().url(key).build()
        val call = client.newCall(request)

        return suspendCoroutine<ByteArray> { continuation ->
            call.enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) =
                    continuation.resumeWithException(e)

                override fun onResponse(call: Call, response: Response) {
                    response.body()?.bytes().handle(
                        ifNotNull = continuation::resume,
                        ifNull = { continuation.resumeWithException(IOException("Null response")) }
                    )
                }

            })
        }

    }

}