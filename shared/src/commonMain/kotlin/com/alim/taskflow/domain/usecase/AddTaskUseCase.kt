package com.alim.taskflow.domain.usecase

import com.alim.taskflow.domain.model.Priority
import com.alim.taskflow.domain.model.Task
import com.alim.taskflow.domain.model.TaskStatus
import com.alim.taskflow.domain.repository.TaskRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Use case for creating a new task with validation
 */
class AddTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String?,
        priority: Priority,
        status: TaskStatus = TaskStatus.TODO,
        dueDate: Instant? = null
    ): Result<Task> {
        // Validate title
        Task.validate(title).onFailure { return Result.failure(it) }

        // Create task with generated ID
        val task = Task(
            id = generateTaskId(),
            title = title.trim(),
            description = description?.trim()?.takeIf { it.isNotEmpty() },
            priority = priority,
            status = status,
            dueDate = dueDate,
            createdAt = Clock.System.now()
        )

        return repository.upsertTask(task).map { task }
    }

    private fun generateTaskId(): String {
        return "task_${Clock.System.now().toEpochMilliseconds()}_${(0..9999).random()}"
    }
}