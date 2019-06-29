package com.sibedge.yokodzun.android.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.hnau.androidutils.context_getters.dp_px.DpPxGetter
import ru.hnau.androidutils.context_getters.dp_px.dp0
import ru.hnau.androidutils.ui.utils.logD
import ru.hnau.androidutils.ui.view.list.base.BaseListPaddingDecoration
import ru.hnau.androidutils.ui.view.utils.isLTR
import ru.hnau.androidutils.ui.view.utils.isVertical
import ru.hnau.androidutils.ui.view.utils.linearLayoutManager


class BaseListPaddingDecorationFix(
    private val beforeFirst: DpPxGetter = dp0,
    private val afterLast: DpPxGetter = dp0
) : RecyclerView.ItemDecoration() {

    companion object {

        fun beforeFirst(beforeFirst: DpPxGetter) =
            BaseListPaddingDecoration(beforeFirst = beforeFirst)

        fun afterLast(afterLast: DpPxGetter) =
            BaseListPaddingDecoration(afterLast = afterLast)

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        logD("getItemOffsets")

        val context = parent.context
        val linearLayoutManager = parent.linearLayoutManager
        val isVertical = linearLayoutManager.isVertical
        val isReversed = linearLayoutManager.reverseLayout

        val position = parent.getChildAdapterPosition(view)

        val first = position <= 0
        if (first) {
            val beforeFirst = beforeFirst.getPxInt(context)
            if (isVertical) {
                if (isReversed) {
                    outRect.bottom = beforeFirst
                } else {
                    outRect.top = beforeFirst
                }
            } else {
                if (isReversed || !isLTR) {
                    outRect.right = beforeFirst
                } else {
                    outRect.left = beforeFirst
                }
            }
            return
        }

        val last = position >= (parent.adapter?.itemCount ?: 0) - 1
        if (last) {
            val afterLast = afterLast.getPxInt(context)
            if (isVertical) {
                if (isReversed) {
                    outRect.top = afterLast
                } else {
                    outRect.bottom = afterLast
                }
            } else {
                if (isReversed || !isLTR) {
                    outRect.left = afterLast
                } else {
                    outRect.right = afterLast
                }
            }
        }
    }

}