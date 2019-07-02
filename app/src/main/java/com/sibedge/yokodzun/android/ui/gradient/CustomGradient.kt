package com.sibedge.yokodzun.android.ui.gradient

import ru.hnau.androidutils.context_getters.ColorGetter
import ru.hnau.androidutils.ui.utils.types_utils.ColorUtils
import ru.hnau.jutils.ifTrue


class CustomGradient(
    private val startColor: ColorGetter,
    keyPoints: Array<KeyPoint>,
    private val endColor: ColorGetter
) {

    companion object {

        private const val START = 0f
        private const val END = 1f

    }

    data class KeyPoint(
        val position: Float,
        val color: ColorGetter
    )

    private val points =
        (keyPoints.toList().filter { point -> point.position > START && point.position < END } +
                KeyPoint(START, startColor) + KeyPoint(END, endColor))
            .sortedBy { it.position }

    fun getColor(position: Float): ColorGetter {
        (position <= START).ifTrue { return startColor }
        lateinit var beforePoint: KeyPoint
        points.forEach { point ->
            (position == point.position).ifTrue { return point.color }
            if (position < point.position) {
                return beforePoint.color.mapInterThisAndOther(
                    other = point.color,
                    pos = (position - beforePoint.position) / (point.position - beforePoint.position)
                )
            }
            beforePoint = point
        }
        return endColor
    }

}