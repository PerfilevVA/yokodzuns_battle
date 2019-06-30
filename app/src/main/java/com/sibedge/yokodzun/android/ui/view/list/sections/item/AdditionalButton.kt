package com.sibedge.yokodzun.android.ui.view.list.sections.item

import android.content.Context
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.common.data.battle.Section
import ru.hnau.androidutils.context_getters.DrawableGetter
import ru.hnau.androidutils.ui.view.clickable.ClickableLayoutDrawableView
import ru.hnau.androidutils.ui.view.utils.setInvisible
import ru.hnau.androidutils.ui.view.utils.setVisible
import ru.hnau.jutils.handle


class AdditionalButton(
    context: Context,
    private val additionalButtonInfo: (Section) -> Info?
) : ClickableLayoutDrawableView(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO,
    initialContent = DrawableGetter.EMPTY
) {

    data class Info(
        val icon: DrawableGetter,
        val action: () -> Unit
    )

    init {
        setInvisible()
    }

    private var info: Info? = null
        set(value) {
            field = value
            value.handle(
                ifNotNull = {
                    setVisible()
                    content = it.icon
                },
                ifNull = {
                    setInvisible()
                }
            )
        }

    fun setSection(section: Section?) {
        info = section?.let(additionalButtonInfo)
    }

    override fun onClick() {
        super.onClick()
        info?.action?.invoke()
    }


}