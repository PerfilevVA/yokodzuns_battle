package com.sibedge.yokodzun.common.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.hnau.jutils.JUtils
import ru.hnau.jutils.TimeValue
import ru.hnau.jutils.takeIfNotEmpty
import java.lang.reflect.Type
import java.util.*

val RANDOM = Random()

val GSON = Gson()

val TimeValue.uiString: String
    get() = listOf(
            daysDigits to "д",
            hoursDigits to "ч",
            minutesDigits to "м",
            secondsDigits to "с",
            millisecondsDigits to "мс"
    )
            .filter { it.first > 0 }
            .takeIfNotEmpty()
            ?.joinToString(
                    separator = " ",
                    transform = { "${it.first}${it.second}" }
            ) ?: "0мс"