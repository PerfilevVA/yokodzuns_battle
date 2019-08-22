package com.sibedge.yokodzun.common.data.notification

import com.sibedge.yokodzun.common.utils.GSON


abstract class YNotification {

    data class Serialized(
            val className: String,
            val json: String
    )

    companion object {

        private const val NOTIFICATION_CLASS_NAME_PREFIX = "com.sibedge.team.common.data.notification.type"

        fun deserialize(
                className: String,
                json: String
        ): YNotification {
            val contentClassName = "$NOTIFICATION_CLASS_NAME_PREFIX.$className"
            val contentClass = Class.forName(contentClassName)
            return GSON.fromJson(json, contentClass) as YNotification
        }

    }

    fun serialize() = Serialized(
            className = javaClass.simpleName,
            json = GSON.toJson(this)
    )

    protected open fun generateDescription(): String? = null

    override fun toString() =
            "${javaClass.simpleName}${generateDescription()?.let { "($it)" } ?: ""}"

}