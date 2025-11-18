package com.alim.taskflow.domain.usecase

import com.alim.taskflow.domain.model.FilterOptions
import com.alim.taskflow.domain.model.SortOption
import com.alim.taskflow.domain.model.Task
import com.alim.taskflow.domain.repository.TaskRepository
import kotlinx.datetime.Clock

/**
 * Use case for filtering and sorting tasks
 * Handles complex business logic for filtering overdue tasks
 */
class FilterTasksUseCase(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        filterOptions: FilterOptions,
        sortOption: SortOption = SortOption.DATE_CREATED_DESC
    ): Result<List<Task>> {
        val result = repository.getFilteredTasks(filterOptions, sortOption)

        return result.map { tasks ->
            // Apply overdue filter if needed (business logic)
            if (filterOptions.showOverdueOnly) {
                val now = Clock.System.now()
                tasks.filter { it.isOverdue(now) }
            } else {
                tasks
            }
        }
    }
}