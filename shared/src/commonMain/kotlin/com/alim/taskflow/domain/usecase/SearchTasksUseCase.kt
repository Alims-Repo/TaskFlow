package com.alim.taskflow.domain.usecase

import com.alim.taskflow.domain.model.Task
import com.alim.taskflow.domain.repository.TaskRepository

/**
 * Use case for searching tasks by title
 */
class SearchTasksUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(query: String): Result<List<Task>> {
        if (query.isBlank()) {
            return repository.getAllTasks()
        }
        return repository.searchTasks(query.trim())
    }
}