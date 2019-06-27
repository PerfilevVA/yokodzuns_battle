package com.sibedge.yokodzun.android.layers.students_group_test_results


data class StudentsGroupTestResultsListItem(
    val studentIdentifier: String,
    val score: Float,
    val maxScore: Int,
    val passed: Boolean
)