package com.alim.taskflow.domain.repository

import com.alim.taskflow.domain.model.*
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface - defines contract for data operations.
 * This keeps the domain layer independent of implementation details.
 */
interface TaskRepository {

    /**
     * Observe all tasks as a Flow for reactive updates
     */
    fun observeAllTasks(): Flow<List<Task>>

    /**
     * Get all tasks (one-time fetch)
     */
    suspend fun getAllTasks(): Result<List<Task>>

    /**
     * Get a single task by ID
     */
    suspend fun getTaskById(id: String): Result<Task?>

    /**
     * Search tasks by title
     */
    suspend fun searchTasks(query: String): Result<List<Task>>

    /**
     * Get filtered and sorted tasks
     */
    suspend fun getFilteredTasks(
        filterOptions: FilterOptions,
        sortOption: SortOption
    ): Result<List<Task>>

    /**
     * Get tasks by status
     */
    suspend fun getTasksByStatus(status: TaskStatus): Result<List<Task>>

    /**
     * Get tasks by priority
     */
    suspend fun getTasksByPriority(priority: Priority): Result<List<Task>>

    /**
     * Insert or update a task
     */
    suspend fun upsertTask(task: Task): Result<Unit>

    /**
     * Delete a task
     */
    suspend fun deleteTask(id: String): Result<Unit>

    /**
     * Delete all tasks
     */
    suspend fun deleteAllTasks(): Result<Unit>

    /**
     * Get count of tasks
     */
    suspend fun getTaskCount(): Result<Long>

    /**
     * Get count of tasks by status
     */
    suspend fun getTaskCountByStatus(status: TaskStatus): Result<Long>
}