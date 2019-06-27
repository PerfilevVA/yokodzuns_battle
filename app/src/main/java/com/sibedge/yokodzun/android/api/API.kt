package com.sibedge.yokodzun.android.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.sibedge.yokodzun.android.api.response_handler.DeferredCallAdapterFactory
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.utils.managers.SettingsManager
import ru.hnau.remote_teaching_common.api.ApiUtils
import ru.hnau.remote_teaching_common.exception.ApiException


var cached: RTService? = null

val API: RTService
    get() {
        var result = cached
        if (result == null) {
            result = getApi()
            cached = result
        }
        return result
    }

fun resetApi() {
    cached = null
}

private fun getApi(): RTService {

    val host = SettingsManager.host
    if (host.isEmpty()) {
        throw ApiException.HOST_NOT_CONFIGURED
    }

    val httpClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            chain.request().let { request ->
                chain.proceed(
                    request.newBuilder().apply {
                        header(ApiUtils.HEADER_CLIENT_TYPE, ApiConstants.CLIENT_TYPE_NAME)
                        header(ApiUtils.HEADER_CLIENT_VERSION, ApiConstants.CLIENT_VERSION.toString())
                        header(ApiUtils.HEADER_AUTH_TOKEN, AuthManager.token ?: "")
                        method(request.method(), request.body())
                    }.build()
                )
            }
        }
    }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .addCallAdapterFactory(DeferredCallAdapterFactory)
        .build()

    return retrofit.create(RTService::class.java)
}