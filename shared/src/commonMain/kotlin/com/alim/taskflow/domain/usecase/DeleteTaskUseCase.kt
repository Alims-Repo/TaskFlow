package com.alim.taskflow.domain.usecase

import com.alim.taskflow.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteTask(id)
    }
}