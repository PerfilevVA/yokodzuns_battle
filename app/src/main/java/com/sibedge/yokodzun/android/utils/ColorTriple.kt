package com.sibedge.yokodzun.android.utils

import ru.hnau.androidutils.context_getters.ColorGetter


data class ColorTriple(
    val light: ColorGetter,
    val main: ColorGetter,
    val dark: ColorGetter
)

fun ColorGetter.toTriple() = ColorTriple(this, this, this)