package com.alim.taskflow.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alim.taskflow.domain.usecase.GetTaskByIdUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Shared ViewModel for task detail operations
 */
class TaskDetailViewModel(
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TaskDetailUiState>(TaskDetailUiState.Loading)
    val uiState: StateFlow<TaskDetailUiState> = _uiState.asStateFlow()

    fun loadTask(id: String) {
        viewModelScope.launch {
            _uiState.value = TaskDetailUiState.Loading
            getTaskByIdUseCase(id)
                .onSuccess { task ->
                    _uiState.value = if (task != null) {
                        TaskDetailUiState.Success(task)
                    } else {
                        TaskDetailUiState.Error("Task not found")
                    }
                }
                .onFailure { error ->
                    _uiState.value = TaskDetailUiState.Error(error.message ?: "Failed to load task")
                }
        }
    }
}