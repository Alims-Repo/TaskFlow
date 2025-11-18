package com.alim.taskflow.domain.model

data class TaskStatistics(
    val total: Long,
    val todo: Long,
    val inProgress: Long,
    val done: Long
) {
    val completionRate: Float
        get() = if (total > 0) done.toFloat() / total.toFloat() else 0f
}