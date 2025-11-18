package com.alim.taskflow.domain.usecase

import com.alim.taskflow.domain.model.Task
import com.alim.taskflow.domain.repository.TaskRepository

class GetTaskByIdUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: String): Result<Task?> {
        return repository.getTaskById(id)
    }
}
