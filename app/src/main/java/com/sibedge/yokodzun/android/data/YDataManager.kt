package com.sibedge.yokodzun.android.data

import com.sibedge.yokodzun.common.data.battle.Battle
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.getter.SuspendGetter
import ru.hnau.jutils.getter.SuspendMutableGetter
import ru.hnau.jutils.getter.toSuspendGetter
import ru.hnau.jutils.handle
import ru.hnau.jutils.producer.CachingProducer


abstract class YDataManager<T : Any>(
    valueLifetime: TimeValue? = DataUtils.DEFAULT_VALUE_LIFETIME
) : CachingProducer<SuspendGetter<Unit, T>>(
    valueLifetime = valueLifetime
) {

    val existenceValue: T?
        get() = existence?.existence

    init {
        AuthManager.onUserLoggedProducer.attach { invalidate() }
    }

    protected suspend abstract fun getValue(): T

    override fun getNewValue() =
        SuspendGetter.simple(this::getValue)

    fun updateValue(newValue: T) =
        update(newValue.toSuspendGetter())

    protected fun updateOrInvalidate(updater: (lastValue: T) -> T) {
        val getter = existence
        val existence = getter?.existence
        if (existence == null) {
            invalidate()
            return
        }
        val newValue = updater.invoke(existence)
        updateValue(newValue)
    }

}