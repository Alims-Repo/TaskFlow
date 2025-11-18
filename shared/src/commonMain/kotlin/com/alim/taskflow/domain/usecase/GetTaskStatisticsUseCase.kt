package com.alim.taskflow.domain.usecase

import com.alim.taskflow.domain.model.TaskStatistics
import com.alim.taskflow.domain.model.TaskStatus
import com.alim.taskflow.domain.repository.TaskRepository

/**
 * Use case for retrieving task statistics
 */
class GetTaskStatisticsUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(): Result<TaskStatistics> {
        return try {
            val total = repository.getTaskCount().getOrThrow()
            val todo = repository.getTaskCountByStatus(TaskStatus.TODO).getOrThrow()
            val inProgress = repository.getTaskCountByStatus(TaskStatus.IN_PROGRESS).getOrThrow()
            val done = repository.getTaskCountByStatus(TaskStatus.DONE).getOrThrow()

            Result.success(
                TaskStatistics(
                    total = total,
                    todo = todo,
                    inProgress = inProgress,
                    done = done
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}