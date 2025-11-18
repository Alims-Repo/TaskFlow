package com.alim.taskflow.domain.model

enum class TaskStatus(val value: Int, val displayName: String) {
    TODO(0, "To Do"),
    IN_PROGRESS(1, "In Progress"),
    DONE(2, "Done");

    companion object {
        fun fromValue(value: Int): TaskStatus {
            return entries.find { it.value == value } ?: TODO
        }
    }
}