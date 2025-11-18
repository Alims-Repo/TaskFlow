package com.alim.taskflow.domain.usecase

import com.alim.taskflow.domain.model.Priority
import com.alim.taskflow.domain.model.Task
import com.alim.taskflow.domain.model.TaskStatus
import com.alim.taskflow.domain.repository.TaskRepository
import kotlinx.datetime.Instant

class UpdateTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        id: String,
        title: String,
        description: String?,
        priority: Priority,
        status: TaskStatus,
        dueDate: Instant?
    ): Result<Task> {
        // Validate title
        Task.validate(title).onFailure { return Result.failure(it) }

        // Get existing task to preserve createdAt
        val existingTask = repository.getTaskById(id).getOrNull()
            ?: return Result.failure(IllegalArgumentException("Task not found"))

        // Create updated task
        val updatedTask = existingTask.copy(
            title = title.trim(),
            description = description?.trim()?.takeIf { it.isNotEmpty() },
            priority = priority,
            status = status,
            dueDate = dueDate
        )

        return repository.upsertTask(updatedTask).map { updatedTask }
    }

    /**
     * Quick status update without fetching full task
     */
    suspend fun updateStatus(id: String, status: TaskStatus): Result<Unit> {
        val task = repository.getTaskById(id).getOrNull()
            ?: return Result.failure(IllegalArgumentException("Task not found"))

        return repository.upsertTask(task.copy(status = status))
    }
}