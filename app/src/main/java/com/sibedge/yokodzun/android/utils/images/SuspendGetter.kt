package com.sibedge.yokodzun.android.utils.images


interface SuspendGetter<K, R> {

    suspend fun get(key: K): R

}