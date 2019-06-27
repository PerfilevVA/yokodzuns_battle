package com.sibedge.yokodzun.android.ui.cell.properties

import android.content.Context
import android.widget.LinearLayout
import ru.hnau.androidutils.context_getters.StringGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager

class PropertiesCellPropertyView<T : Any>(
    context: Context,
    private val data: Data<T>
) : LinearLayout(
    context
) {

    data class Data<T : Any>(
        val name: StringGetter,
        val valueExtractor: (T) -> StringGetter
    )


    var item: T? = null
        set(value) {
            if (field != value && value != null) {
                field = value
                onItemChanged(value)
            }
        }

    private val nameView = Label(
        context = context,
        fontType = FontManager.DEFAULT,
        gravity = HGravity.START_CENTER_VERTICAL,
        textColor = ColorManager.FG_T50,
        textSize = SizeManager.TEXT_12,
        minLines = 1,
        maxLines = 1,
        initialText = data.name
    )
        .applyLinearParams {
            setStretchedWidth()
            setEndMargin(SizeManager.SMALL_SEPARATION)
        }

    private val valueView = Label(
        context = context,
        fontType = FontManager.DEFAULT,
        gravity = HGravity.END_CENTER_VERTICAL,
        textColor = ColorManager.FG_T50,
        textSize = SizeManager.TEXT_12,
        minLines = 1,
        maxLines = 1
    )

    init {
        orientation = HORIZONTAL
        addChild(nameView)
        addChild(valueView)
    }

    private fun onItemChanged(item: T) {
        valueView.text = data.valueExtractor(item)
    }

}