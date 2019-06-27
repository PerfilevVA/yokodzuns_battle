package com.sibedge.yokodzun.android.ui.plus_minus

import android.util.Range
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.jutils.producer.Producer


data class PlusMinusColumnInfo<T>(
    val title: StringGetter,
    val actionPlus: (T) -> T,
    val actionMinus: (T) -> T
)