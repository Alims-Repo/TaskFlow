package com.alim.taskflow.domain.usecase

import com.alim.taskflow.domain.model.Task
import com.alim.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case for retrieving all tasks with reactive updates
 */
class GetAllTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.observeAllTasks()
    }

    suspend fun execute(): Result<List<Task>> {
        return repository.getAllTasks()
    }
}