package com.sibedge.yokodzun.android.data.entity

import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.toGetter


interface EntityWithId<T> {

    val number: Int

    val value: T

    val numberUiString: String
        get() = (number + 1).toString()

}