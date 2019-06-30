package com.sibedge.yokodzun.android.ui.view.plus_minus

import ru.hnau.androidutils.context_getters.StringGetter


data class PlusMinusColumnInfo<T>(
    val title: StringGetter,
    val actionPlus: (T) -> T,
    val actionMinus: (T) -> T
)