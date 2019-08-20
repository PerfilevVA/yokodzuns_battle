package com.sibedge.yokodzun.common.data.notification


data class YToUserNotification(
        val toUser: String,
        val notification: YNotification.Serialized
) {

    companion object {

        const val SERIALIZATION_KEY_TO_USER = "toUser"
        const val SERIALIZATION_KEY_CLASS = "class"
        const val SERIALIZATION_KEY_CONTENT = "content"

    }

}