package com.alim.taskflow.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "TaskEntity",
    indices = [
        Index(value = ["status"]),
        Index(value = ["priority"]),
        Index(value = ["dueDate"]),
        Index(value = ["createdAt"]),
        Index(value = ["title"])
    ]
)
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String? = null,
    val priority: Int = 0,
    val status: Int = 0,
    val dueDate: Long? = null,
    val createdAt: Long
)