package com.sibedge.yokodzun.common.exception


sealed class ApiExceptionContent {

    open class Common(
            val message: String
    ) : ApiExceptionContent() {

        override fun generateDescription() =
                "message=$message"

    }

    object Undefined : Common("Неизвестная ошибка")

    object Authentication : ApiExceptionContent()

    open class DdosBlocked(
            val secondsToUnlock: Long?
    ) : ApiExceptionContent() {

        override fun generateDescription() =
                "secondsToUnlock=$secondsToUnlock"

    }

    class DdosBlockedIp(
            secondsToUnlock: Long?
    ) : DdosBlocked(secondsToUnlock)

    class DdosBlockedUser(
            secondsToUnlock: Long?
    ) : DdosBlocked(secondsToUnlock)

    object AdminPasswordNotConfigured : ApiExceptionContent()

    object HostNotConfigured : ApiExceptionContent()

    object Network : Common("Ошибка связи с сервером")

    protected open fun generateDescription(): String? = null

    override fun toString() =
            "${javaClass.simpleName}${generateDescription()?.let { "($it)" } ?: ""}"

}