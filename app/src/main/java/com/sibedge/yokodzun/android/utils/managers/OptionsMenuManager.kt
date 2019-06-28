package com.sibedge.yokodzun.android.utils.managers

import android.os.Build
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import ru.hnau.androidutils.context_getters.StringGetter
import com.sibedge.yokodzun.android.R


object OptionsMenuManager {

    data class Item(
        val title: StringGetter,
        val onClick: () -> Unit
    )

    fun show(
        anchor: View,
        items: Iterable<Item>
    ) {
        val popupMenu = createMenu(anchor)
        items.forEach { (title, onClick) ->
            popupMenu.menu.add(title.get(anchor.context)).apply {
                setOnMenuItemClickListener { onClick.invoke(); true }
            }
        }
        popupMenu.show()
    }

    private fun createMenu(anchor: View): PopupMenu {
        val contextWrapper = ContextThemeWrapper(anchor.context, R.style.Theme_AppCompat_DayNight)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            PopupMenu(contextWrapper, anchor, Gravity.TOP or Gravity.RIGHT or Gravity.END)
        } else {
            PopupMenu(contextWrapper, anchor)
        }
    }

}