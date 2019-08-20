package com.sibedge.yokodzun.common.api

import com.sibedge.yokodzun.common.exception.ApiException
import ru.hnau.jutils.tryOrNull


data class ApiResponse<T : Any>(
        val data: T?,
        val error: ApiError?
) {

    companion object {

        fun <T : Any> success(data: T) =
                ApiResponse(data = data, error = null)

        fun <T : Any> error(exception: ApiException) =
                ApiResponse<T>(data = null, error = exception.serialize())

    }

    fun <R : Any> handle(
            onSuccess: (T) -> R,
            onError: (ApiException) -> R
    ): R {

        data?.let { return onSuccess.invoke(it) }

        error?.let { error ->
            val exception = tryOrNull { ApiException.deserialize(error) }
                    ?: ApiException.UNDEFINED
            return onError.invoke(exception)
        }

        return onError.invoke(ApiException.UNDEFINED)

    }

}