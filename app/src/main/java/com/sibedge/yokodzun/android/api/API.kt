package com.sibedge.yokodzun.android.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.sibedge.yokodzun.android.api.response_handler.DeferredCallAdapterFactory
import com.sibedge.yokodzun.android.data.AuthManager
import com.sibedge.yokodzun.android.utils.managers.SettingsManager
import com.sibedge.yokodzun.common.api.ApiUtils
import com.sibedge.yokodzun.common.exception.ApiException
import ru.hnau.jutils.takeIfNotEmpty


var cached: YService? = null

val API: YService
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

private fun getApi(): YService {

    val host = SettingsManager.host
    if (host.isEmpty()) {
        throw ApiException.HOST_NOT_CONFIGURED
    }

    val httpClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            chain.request().let { request ->
                chain.proceed(
                    request.newBuilder().apply {

                        AuthManager.adminAuthToken.takeIfNotEmpty()
                            ?.let { header(ApiUtils.HEADER_ADMIN_AUTH_TOKEN, it) }

                        AuthManager.raterCode.takeIfNotEmpty()
                            ?.let { header(ApiUtils.HEADER_RATER_CODE, it) }

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

    return retrofit.create(YService::class.java)
}