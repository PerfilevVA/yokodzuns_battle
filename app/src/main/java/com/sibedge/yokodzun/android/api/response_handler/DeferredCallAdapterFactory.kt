package com.sibedge.yokodzun.android.api.response_handler

import com.google.gson.reflect.TypeToken
import com.sibedge.yokodzun.common.api.ApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.CallAdapter
import retrofit2.Retrofit
import ru.hnau.jutils.possible.Possible
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


object DeferredCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (Deferred::class.java != getRawType(returnType)) {
            return null
        }

        val deferredType = returnType as? ParameterizedType
            ?: throw IllegalStateException("Deferred return type must be parameterized as Deferred<Foo> or Deferred<out Foo>")

        val responseTypeRaw = getParameterUpperBound(0, deferredType)

        val responseType = TypeToken.getParameterized(ApiResponse::class.java, responseTypeRaw).type
        return DeferredCallAdapter<Any>(responseType)

    }

}