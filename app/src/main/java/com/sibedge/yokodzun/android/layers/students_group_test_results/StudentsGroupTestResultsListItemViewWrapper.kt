package com.sibedge.yokodzun.android.layers.students_group_test_results

import android.content.Context
import ru.hnau.androidutils.context_getters.toGetter
import ru.hnau.androidutils.ui.utils.h_gravity.HGravity
import ru.hnau.androidutils.ui.view.label.Label
import ru.hnau.androidutils.ui.view.utils.apply.addChild
import ru.hnau.androidutils.ui.view.utils.apply.applyHorizontalOrientation
import ru.hnau.androidutils.ui.view.utils.apply.applyPadding
import ru.hnau.androidutils.ui.view.utils.apply.layout_params.applyLinearParams
import ru.hnau.jutils.handle
import com.sibedge.yokodzun.android.ui.cell.Cell
import com.sibedge.yokodzun.android.ui.cell.label.CellLabel
import com.sibedge.yokodzun.android.ui.cell.label.CellTitle
import com.sibedge.yokodzun.android.utils.managers.ColorManager
import com.sibedge.yokodzun.android.utils.managers.FontManager
import com.sibedge.yokodzun.android.utils.managers.SizeManager


class StudentsGroupTestResultsListItemViewWrapper(
    context: Context,
    onClick: (StudentsGroupTestResultsListItem) -> Unit
) : Cell<StudentsGroupTestResultsListItem>(
    context = context,
    rippleDrawInfo = ColorManager.PRIMARY_ON_TRANSPARENT_RIPPLE_INFO,
    onClick = onClick
) {

    private val titleView = CellTitle(
        context = context,
        activeColor = ColorManager.PRIMARY,
        inactiveColor = ColorManager.FG_T50
    )
        .applyLinearParams {
            setStretchedWidth()
            setEndMargin(SizeManager.DEFAULT_SEPARATION)
        }

    private val resultView = Label(
        context = context,
        fontType = FontManager.DEFAULT,
        textSize = SizeManager.TEXT_16,
        minLines = 1,
        maxLines = 1,
        gravity = HGravity.END_CENTER_VERTICAL
    )
        .applyLinearParams()

    init {
        applyHorizontalOrientation()
        applyPadding(SizeManager.DEFAULT_SEPARATION)
        addChild(titleView)
        addChild(resultView)
    }

    override fun onContentReceived(content: StudentsGroupTestResultsListItem) {
        titleView.info = CellLabel.Info(
            text = content.studentIdentifier.toGetter(),
            active = content.passed
        )
        resultView.text = "${content.score}/${content.maxScore}".toGetter()
        resultView.textColor = content.passed.handle(
            forTrue = ColorManager.GOOD,
            forFalse = ColorManager.BAD
        )
    }

}