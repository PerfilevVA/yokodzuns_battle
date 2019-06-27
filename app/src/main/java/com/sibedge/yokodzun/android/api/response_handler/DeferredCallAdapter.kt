package com.sibedge.yokodzun.android.api.response_handler

import com.sibedge.yokodzun.common.api.ApiResponse
import com.sibedge.yokodzun.common.exception.ApiException
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import ru.hnau.androidutils.ui.utils.logD
import ru.hnau.androidutils.ui.utils.logE
import ru.hnau.androidutils.ui.utils.logW
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.coroutines.deferred.deferred
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicLong


class DeferredCallAdapter<T : Any>(
    private val responseType: Type
) : CallAdapter<ApiResponse<T>, Deferred<T>> {

    companion object {

        private val requestId = AtomicLong(0L)

    }

    private val Call<ApiResponse<T>>.logString: String
        get() = listOfNotNull(
            request().method(),
            request().url().url().toString()
        ).joinToString(separator = " ")

    override fun responseType() = responseType

    private fun createLogPrefix(tag: String, requestId: Long, started: TimeValue) =
        listOfNotNull(
            requestId.toString(),
            tag,
            (TimeValue.now() - started).takeIf { it > TimeValue.ZERO }?.toLevelsString()
        ).joinToString(separator = "|", prefix = "[", postfix = "]")

    override fun adapt(call: Call<ApiResponse<T>>) = deferred<T> {

        val requestId = requestId.incrementAndGet()
        val started = TimeValue.now()

        this@DeferredCallAdapter.logD("${createLogPrefix("START", requestId, started)} ${call.logString}")

        call.enqueue(object : Callback<ApiResponse<T>> {

            override fun onFailure(call: Call<ApiResponse<T>>, t: Throwable) {
                this@DeferredCallAdapter.logE(createLogPrefix("FAILURE", requestId, started), t)
                completeExceptionally(ApiException.NETWORK)
            }

            override fun onResponse(call: Call<ApiResponse<T>>, response: Response<ApiResponse<T>>) {
                if (!response.isSuccessful) {
                    this@DeferredCallAdapter.logW(
                        "${createLogPrefix(
                            "HTTP ERROR",
                            requestId,
                            started
                        )} code=${response.code()}, message=${response.message()}, body=${response.errorBody()?.string()}"
                    )
                    completeExceptionally(ApiException.NETWORK)
                    return
                }
                response.body()!!.handle(
                    onSuccess = { responseData ->
                        this@DeferredCallAdapter.logD("${createLogPrefix("SUCCESS", requestId, started)} response=$responseData")
                        complete(responseData)
                    },
                    onError = { apiException ->
                        this@DeferredCallAdapter.logW(createLogPrefix("API ERROR", requestId, started), apiException)
                        completeExceptionally(apiException)
                    }
                )
            }
        })

        invokeOnCompletion {
            if (isCancelled) {
                call.cancel()
            }
        }

    }

}