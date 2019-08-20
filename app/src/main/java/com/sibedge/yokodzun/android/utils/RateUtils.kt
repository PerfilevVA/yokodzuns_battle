package com.sibedge.yokodzun.android.utils

import com.sibedge.yokodzun.android.ui.gradient.CustomGradient
import com.sibedge.yokodzun.android.utils.managers.ColorManager


object RateUtils {

    private val MARK_GRADIENT = CustomGradient(
        startColor = ColorManager.RED_TRIPLE.main,
        keyPoints = arrayOf(
            CustomGradient.KeyPoint(
                color = ColorManager.ORANGE_TRIPLE.main,
                position = 0.5f
            )
        ),
        endColor = ColorManager.GREEN_TRIPLE.main
    )

    private val UNTOUCHABLE_MARK_GRADIENT = CustomGradient(
        startColor = ColorManager.GREY_TRIPLE.light,
        keyPoints = arrayOf(
            CustomGradient.KeyPoint(
                color = ColorManager.GREY_TRIPLE.main,
                position = 0.5f
            )
        ),
        endColor = ColorManager.GREY_TRIPLE.dark
    )

    const val MARKS_COUNT = 5

    private const val MIN_MARK_VALUE = 1
    private const val MAX_MARK_VALUE = MIN_MARK_VALUE + MARKS_COUNT - 1

    fun valueToMark(value: Float) =
        MIN_MARK_VALUE + (MAX_MARK_VALUE - MIN_MARK_VALUE) * value

    fun markToValue(mark: Float) =
        (mark - MIN_MARK_VALUE) / (MAX_MARK_VALUE - MIN_MARK_VALUE)

    fun getValueColor(value: Float) =
        MARK_GRADIENT.getColor(value)

    fun getMarkColor(mark: Float) =
        getValueColor(markToValue(mark))

    fun getUntouchableValueColor(value: Float) =
        UNTOUCHABLE_MARK_GRADIENT.getColor(value)

    fun getUntouchableMarkColor(mark: Float) =
        getUntouchableValueColor(markToValue(mark))
}