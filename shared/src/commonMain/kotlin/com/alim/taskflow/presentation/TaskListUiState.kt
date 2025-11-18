package com.alim.taskflow.presentation

import com.alim.taskflow.domain.model.Task

/**
 * UI State sealed class for better state management
 */
sealed class TaskListUiState {
    data object Loading : TaskListUiState()
    data class Success(val tasks: List<Task>) : TaskListUiState()
    data class Error(val message: String) : TaskListUiState()
}