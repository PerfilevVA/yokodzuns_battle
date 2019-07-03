package com.sibedge.yokodzun.android.utils.images

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.hnau.jutils.handle


class CachingSuspendLambda<R>(
    private val getter: suspend () -> R
) {

    private val mutex = Mutex()

    private var cachedResultExists = false
    private var cachedResult: R? = null

    suspend fun get() = mutex.withLock<R> {
        cachedResultExists.handle(
            onTrue = { cachedResult!! },
            onFalse = {
                val result = getter()
                cachedResult = result
                cachedResultExists = true
                return@handle result
            }
        )
    }

}