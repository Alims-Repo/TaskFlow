package com.alim.taskflow.data.local.database.dao

import androidx.room.*
import com.alim.taskflow.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskEntity ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM TaskEntity ORDER BY createdAt DESC")
    suspend fun selectAll(): List<TaskEntity>

    @Query("SELECT * FROM TaskEntity WHERE id = :id")
    suspend fun selectById(id: String): TaskEntity?

    @Query("SELECT * FROM TaskEntity WHERE status = :status ORDER BY createdAt DESC")
    suspend fun selectByStatus(status: Int): List<TaskEntity>

    @Query("SELECT * FROM TaskEntity WHERE priority = :priority ORDER BY createdAt DESC")
    suspend fun selectByPriority(priority: Int): List<TaskEntity>

    @Query("SELECT * FROM TaskEntity WHERE title LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    suspend fun searchByTitle(query: String): List<TaskEntity>

    @Query("""
        SELECT * FROM TaskEntity
        WHERE
            (CASE WHEN :statusFilterEmpty THEN 1 ELSE status IN (:statusFilter) END)
            AND (CASE WHEN :priorityFilterEmpty THEN 1 ELSE priority IN (:priorityFilter) END)
        ORDER BY
            CASE
                WHEN :sortBy = 'DATE_CREATED_DESC' THEN createdAt
                WHEN :sortBy = 'DUE_DATE_ASC' THEN dueDate
            END DESC,
            CASE
                WHEN :sortBy = 'DATE_CREATED_ASC' THEN createdAt
                WHEN :sortBy = 'DUE_DATE_DESC' THEN dueDate
            END ASC,
            CASE
                WHEN :sortBy = 'PRIORITY_HIGH_TO_LOW' THEN priority
            END DESC,
            CASE
                WHEN :sortBy = 'PRIORITY_LOW_TO_HIGH' THEN priority
            END ASC,
            CASE
                WHEN :sortBy = 'TITLE_A_TO_Z' THEN title
            END ASC,
            CASE
                WHEN :sortBy = 'TITLE_Z_TO_A' THEN title
            END DESC
    """)
    suspend fun selectFiltered(
        statusFilterEmpty: Boolean,
        statusFilter: List<Int>,
        priorityFilterEmpty: Boolean,
        priorityFilter: List<Int>,
        sortBy: String
    ): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM TaskEntity WHERE id = :id")
    suspend fun deleteTask(id: String)

    @Query("DELETE FROM TaskEntity")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM TaskEntity")
    suspend fun countAll(): Long

    @Query("SELECT COUNT(*) FROM TaskEntity WHERE status = :status")
    suspend fun countByStatus(status: Int): Long
}