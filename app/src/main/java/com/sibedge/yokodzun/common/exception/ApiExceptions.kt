package com.sibedge.yokodzun.common.exception

import com.sibedge.yokodzun.common.api.ApiError
import com.sibedge.yokodzun.common.utils.GSON
import java.lang.Exception


class ApiException(
        val content: ApiExceptionContent
) : Exception() {

    companion object {

        private fun create(content: ApiExceptionContent) =
                ApiException(content)

        val UNDEFINED = create(ApiExceptionContent.Undefined)

        val AUTHENTICATION = create(ApiExceptionContent.Authentication)

        val ADMIN_PASSWORD_NOT_CONFIGURED = create(ApiExceptionContent.AdminPasswordNotConfigured)

        val HOST_NOT_CONFIGURED = create(ApiExceptionContent.HostNotConfigured)

        val NETWORK = create(ApiExceptionContent.Network)

        fun raw(message: String) =
                create(ApiExceptionContent.Common(message))

        fun ddosBlockedIp(secondsToUnlock: Long?) =
                create(ApiExceptionContent.DdosBlockedIp(secondsToUnlock))

        fun ddosBlockedUser(secondsToUnlock: Long?) =
                create(ApiExceptionContent.DdosBlockedUser(secondsToUnlock))

        fun deserialize(
                apiError: ApiError
        ) = ApiException(
                content = run {
                    val contentClassName = "${ApiExceptionContent::class.java.name}\$${apiError.contentClass}"
                    val clazz = Class.forName(contentClassName)
                    GSON.fromJson(apiError.contentJson, clazz) as ApiExceptionContent
                }
        )

    }

    fun serialize() = ApiError(
            contentClass = content.javaClass.simpleName,
            contentJson = GSON.toJson(content)
    )

    override fun toString() =
            "ApiException(content=$content})"

}