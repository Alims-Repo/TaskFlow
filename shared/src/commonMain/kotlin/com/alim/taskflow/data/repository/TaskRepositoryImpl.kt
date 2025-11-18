package com.alim.taskflow.data.repository

import com.alim.taskflow.data.local.database.dao.TaskDao
import com.alim.taskflow.data.mapper.TaskMapper.toDomain
import com.alim.taskflow.data.mapper.TaskMapper.toEntity
import com.alim.taskflow.domain.model.*
import com.alim.taskflow.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun observeAllTasks(): Flow<List<Task>> {
        return taskDao.observeAll().map { entities ->
            entities.toDomain()
        }
    }

    override suspend fun getAllTasks(): Result<List<Task>> {
        return try {
            val tasks = taskDao.selectAll().toDomain()
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTaskById(id: String): Result<Task?> {
        return try {
            val task = taskDao.selectById(id)?.toDomain()
            Result.success(task)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchTasks(query: String): Result<List<Task>> {
        return try {
            val tasks = taskDao.searchByTitle(query).toDomain()
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFilteredTasks(
        filterOptions: FilterOptions,
        sortOption: SortOption
    ): Result<List<Task>> {
        return try {
            val statusFilter = filterOptions.statuses?.map { it.value } ?: emptyList()
            val priorityFilter = filterOptions.priorities?.map { it.value } ?: emptyList()

            val tasks = taskDao.selectFiltered(
                statusFilterEmpty = statusFilter.isEmpty(),
                statusFilter = statusFilter,
                priorityFilterEmpty = priorityFilter.isEmpty(),
                priorityFilter = priorityFilter,
                sortBy = sortOption.toSortByString()
            ).toDomain()

            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTasksByStatus(status: TaskStatus): Result<List<Task>> {
        return try {
            val tasks = taskDao.selectByStatus(status.value).toDomain()
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTasksByPriority(priority: Priority): Result<List<Task>> {
        return try {
            val tasks = taskDao.selectByPriority(priority.value).toDomain()
            Result.success(tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun upsertTask(task: Task): Result<Unit> {
        return try {
            taskDao.insertTask(task.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(id: String): Result<Unit> {
        return try {
            taskDao.deleteTask(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAllTasks(): Result<Unit> {
        return try {
            taskDao.deleteAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTaskCount(): Result<Long> {
        return try {
            val count = taskDao.countAll()
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTaskCountByStatus(status: TaskStatus): Result<Long> {
        return try {
            val count = taskDao.countByStatus(status.value)
            Result.success(count)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Convert SortOption to database sort string
     */
    private fun SortOption.toSortByString(): String {
        return when (this) {
            SortOption.DATE_CREATED_DESC -> "DATE_CREATED_DESC"
            SortOption.DATE_CREATED_ASC -> "DATE_CREATED_ASC"
            SortOption.DUE_DATE_ASC -> "DUE_DATE_ASC"
            SortOption.DUE_DATE_DESC -> "DUE_DATE_DESC"
            SortOption.PRIORITY_HIGH_TO_LOW -> "PRIORITY_HIGH_TO_LOW"
            SortOption.PRIORITY_LOW_TO_HIGH -> "PRIORITY_LOW_TO_HIGH"
            SortOption.TITLE_A_TO_Z -> "TITLE_A_TO_Z"
            SortOption.TITLE_Z_TO_A -> "TITLE_Z_TO_A"
        }
    }
}