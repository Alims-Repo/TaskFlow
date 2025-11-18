package com.alim.taskflow.presentation

import com.alim.taskflow.domain.model.Task

sealed class TaskDetailUiState {
    data object Loading : TaskDetailUiState()
    data class Success(val task: Task) : TaskDetailUiState()
    data class Error(val message: String) : TaskDetailUiState()
}