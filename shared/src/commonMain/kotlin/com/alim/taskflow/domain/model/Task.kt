package com.alim.taskflow.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Core domain model representing a Task.
 * This is immutable and represents business logic, not storage.
 */
@Serializable
data class Task(
    val id: String,
    val title: String,
    val description: String?,
    val priority: Priority,
    val status: TaskStatus,
    val dueDate: Instant?,
    val createdAt: Instant
) {
    companion object {
        /**
         * Validates task data before creation
         */
        fun validate(title: String): Result<Unit> {
            return when {
                title.isBlank() -> Result.failure(IllegalArgumentException("Title cannot be empty"))
                title.length > 200 -> Result.failure(IllegalArgumentException("Title too long (max 200 chars)"))
                else -> Result.success(Unit)
            }
        }
    }

    /**
     * Business logic: Check if task is overdue
     */
    fun isOverdue(currentTime: Instant): Boolean {
        return dueDate?.let { it < currentTime && status != TaskStatus.DONE } ?: false
    }

    /**
     * Business logic: Check if task is completed
     */
    fun isCompleted(): Boolean = status == TaskStatus.DONE
}