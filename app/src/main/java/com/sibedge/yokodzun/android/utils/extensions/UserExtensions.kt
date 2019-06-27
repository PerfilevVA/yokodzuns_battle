package com.sibedge.yokodzun.android.utils.extensions

import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.jutils.handle
import ru.hnau.jutils.takeIfNotEmpty
import com.sibedge.yokodzun.android.R
import com.sibedge.yokodzun.android.ui.cell.CellAdditionalActionButton
import com.sibedge.yokodzun.android.ui.cell.line.LineCell
import ru.hnau.remote_teaching_common.data.User


val User.fio: String?
    get() = listOf(surname, name, patronymic)
        .mapNotNull { it.takeIfNotEmpty() }
        .joinToString(" ")
        .takeIfNotEmpty()

val User.fioOrLogin: String
    get() = fio ?: login

val User.sortKey: String
    get() = fioOrLogin

fun User.getLineCellData(
    onMenuClick: (() -> Unit)?
): LineCell.Data<User> {

    val additionalActionButtonInfo =
        onMenuClick?.let {
            CellAdditionalActionButton.Info(
                icon = DrawableGetter(R.drawable.ic_options_primary),
                onClick = onMenuClick
            )
        }

    return fio.handle(
        ifNotNull = { fio ->
            LineCell.Data<User>(
                title = fio.toGetter(),
                subtitle = role.title + " ($login)",
                additionalActionButtonInfo = additionalActionButtonInfo
            )
        },
        ifNull = {
            LineCell.Data<User>(
                title = login.toGetter(),
                subtitle = role.title,
                additionalActionButtonInfo = additionalActionButtonInfo
            )
        }
    )
}