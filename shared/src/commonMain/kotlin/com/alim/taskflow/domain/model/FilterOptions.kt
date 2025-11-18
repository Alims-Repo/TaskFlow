package com.alim.taskflow.domain.model

/**
 * Represents filter criteria for tasks
 */
data class FilterOptions(
    val statuses: Set<TaskStatus> = emptySet(),
    val priorities: Set<Priority> = emptySet(),
    val showOverdueOnly: Boolean = false
) {
    fun isEmpty(): Boolean {
        return statuses.isEmpty() && priorities.isEmpty() && !showOverdueOnly
    }
}