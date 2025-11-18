package com.alim.taskflow.domain.model

enum class Priority(val value: Int, val displayName: String) {
    LOW(0, "Low"),
    MEDIUM(1, "Medium"),
    HIGH(2, "High");

    companion object {
        fun fromValue(value: Int): Priority {
            return entries.find { it.value == value } ?: LOW
        }
    }
}