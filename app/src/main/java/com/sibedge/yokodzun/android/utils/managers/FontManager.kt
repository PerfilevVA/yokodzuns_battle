package com.sibedge.yokodzun.android.utils.managers

import ru.hnau.androidutils.ui.font_type.FontTypeGetter


object FontManager {

    val REGULAR = FontTypeGetter("fonts/MontserratAlternates-Medium.ttf")
    val BOLD = FontTypeGetter("fonts/MontserratAlternates-Bold.ttf")

    val DEFAULT = REGULAR

}