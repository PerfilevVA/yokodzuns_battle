package com.sibedge.yokodzun.common.data


enum class ClientType {

    ANDROID;

    val identifier: String
        get() = name

    companion object {

        val supportedIdentifiers =
                values().map { it.identifier }.toHashSet()

    }

}