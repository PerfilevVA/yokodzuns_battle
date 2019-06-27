package com.sibedge.yokodzun.android.ui.cell.label

import android.content.Context
import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.setGone
import ru.hnau.androidutils.ui.view.utils.setVisible
import ru.hnau.jutils.handle
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


open class CellLabel(
    context: Context,
    private val activeColor: ColorGetter,
    private val inactiveColor: ColorGetter,
    textSize: DpPxGetter
) : Label(
    context = context,
    fontType = FontManager.DEFAULT,
    gravity = HGravity.START_CENTER_VERTICAL,
    textSize = textSize,
    minLines = 1,
    maxLines = 1
) {

    data class Info(
        val text: StringGetter?,
        val active: Boolean = true
    )

    var info: Info? = null
        set(value) {
            if (field != value) {
                field = value
                onInfoChanged(value)
            }
        }

    private fun onInfoChanged(info: Info?) {

        val text = info?.text
        if (text == null) {
            setGone()
            return
        }

        setVisible()
        textColor = info.active.handle(
            forTrue = activeColor,
            forFalse = inactiveColor
        )
        this.text = text
    }

}