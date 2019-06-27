package com.sibedge.yokodzun.android.utils.managers

import ru.hnau.androidutils.ui.font_type.FontTypeGetter


object FontManager {

    val UBUNTU = FontTypeGetter("fonts/Ubuntu-R.ttf")
    val UBUNTU_BOLD = FontTypeGetter("fonts/Ubuntu-B.ttf")
    val UBUNTU_MONO = FontTypeGetter("fonts/Ubuntu-M.ttf")

    val DEFAULT = UBUNTU

}