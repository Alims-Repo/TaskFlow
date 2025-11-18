package com.alim.taskflow.data.mapper

import com.alim.taskflow.data.local.entity.TaskEntity
import com.alim.taskflow.domain.model.*
import kotlinx.datetime.Instant
import kotlin.text.toInt
import kotlin.text.toLong

/**
 * Maps between domain models and database entities.
 * Keeps domain layer clean from persistence concerns.
 */
object TaskMapper {

    fun TaskEntity.toDomain(): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            priority = Priority.fromValue(priority),
            status = TaskStatus.fromValue(status),
            dueDate = dueDate?.let { Instant.fromEpochMilliseconds(it) },
            createdAt = Instant.fromEpochMilliseconds(createdAt)
        )
    }

    fun Task.toEntity(): TaskEntity {
        return TaskEntity(
            id = id,
            title = title,
            description = description,
            priority = priority.value,
            status = status.value,
            dueDate = dueDate?.toEpochMilliseconds(),
            createdAt = createdAt.toEpochMilliseconds()
        )
    }

    /**
     * Convert list of TaskEntity to list of Domain Task
     */
    fun List<TaskEntity>.toDomain(): List<Task> {
        return map { it.toDomain() }
    }
}