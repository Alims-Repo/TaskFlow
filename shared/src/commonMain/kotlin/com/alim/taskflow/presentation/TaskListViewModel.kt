package com.alim.taskflow.presentation

import com.alim.taskflow.domain.model.FilterOptions
import com.alim.taskflow.domain.model.Priority
import com.alim.taskflow.domain.model.SortOption
import com.alim.taskflow.domain.model.Task
import com.alim.taskflow.domain.model.TaskStatistics
import com.alim.taskflow.domain.model.TaskStatus
import com.alim.taskflow.domain.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.days

/**
 * Shared ViewModel for task list operations.
 * Can be used from both Android (with Android ViewModel wrapper) and iOS (directly).
 */
class TaskListViewModel(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val searchTasksUseCase: SearchTasksUseCase,
    private val filterTasksUseCase: FilterTasksUseCase,
    private val getTaskStatisticsUseCase: GetTaskStatisticsUseCase
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main + Job())

    // State
    private val _uiState = MutableStateFlow<TaskListUiState>(TaskListUiState.Loading)
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    private val _filterOptions = MutableStateFlow(FilterOptions())
    val filterOptions: StateFlow<FilterOptions> = _filterOptions.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.DATE_CREATED_DESC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statistics = MutableStateFlow<TaskStatistics?>(null)
    val statistics: StateFlow<TaskStatistics?> = _statistics.asStateFlow()

    init {
        observeTasks()
        loadStatistics()
    }

    private fun observeTasks() {
        getAllTasksUseCase()
            .onEach { tasks ->
                _uiState.value = TaskListUiState.Success(tasks)
            }
            .catch { error ->
                _uiState.value = TaskListUiState.Error(error.message ?: "Unknown error")
            }
            .launchIn(viewModelScope)
    }

    fun loadStatistics() {
        viewModelScope.launch {
            getTaskStatisticsUseCase()
                .onSuccess { stats ->
                    _statistics.value = stats
                }
                .onFailure { error ->
                    // Log error but don't fail the whole UI
                    println("Failed to load statistics: ${error.message}")
                }
        }
    }

    fun searchTasks(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            _uiState.value = TaskListUiState.Loading
            searchTasksUseCase(query)
                .onSuccess { tasks ->
                    _uiState.value = TaskListUiState.Success(tasks)
                }
                .onFailure { error ->
                    _uiState.value = TaskListUiState.Error(error.message ?: "Search failed")
                }
        }
    }

    fun applyFilters(
        statuses: Set<TaskStatus> = emptySet(),
        priorities: Set<Priority> = emptySet(),
        showOverdueOnly: Boolean = false
    ) {
        _filterOptions.value = FilterOptions(statuses, priorities, showOverdueOnly)
        loadFilteredTasks()
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        loadFilteredTasks()
    }

    fun clearFilters() {
        _filterOptions.value = FilterOptions()
        _sortOption.value = SortOption.DATE_CREATED_DESC
        _searchQuery.value = ""
        observeTasks()
    }

    private fun loadFilteredTasks() {
        viewModelScope.launch {
            _uiState.value = TaskListUiState.Loading
            filterTasksUseCase(_filterOptions.value, _sortOption.value)
                .onSuccess { tasks ->
                    _uiState.value = TaskListUiState.Success(tasks)
                }
                .onFailure { error ->
                    _uiState.value = TaskListUiState.Error(error.message ?: "Filter failed")
                }
        }
    }

    fun addTask(
        title: String,
        description: String?,
        priority: Priority,
        status: TaskStatus,
        dueDate: Instant?
    ) {
        viewModelScope.launch {
            addTaskUseCase(title, description, priority, status, dueDate)
                .onSuccess {
                    loadStatistics()
                }
                .onFailure { error ->
                    _uiState.value = TaskListUiState.Error(error.message ?: "Failed to add task")
                }
        }
    }

    fun updateTask(
        id: String,
        title: String,
        description: String?,
        priority: Priority,
        status: TaskStatus,
        dueDate: Instant?
    ) {
        viewModelScope.launch {
            updateTaskUseCase(id, title, description, priority, status, dueDate)
                .onSuccess {
                    loadStatistics()
                }
                .onFailure { error ->
                    _uiState.value = TaskListUiState.Error(error.message ?: "Failed to update task")
                }
        }
    }

    fun updateTaskStatus(id: String, status: TaskStatus) {
        viewModelScope.launch {
            updateTaskUseCase.updateStatus(id, status)
                .onSuccess {
                    loadStatistics()
                }
                .onFailure { error ->
                    _uiState.value = TaskListUiState.Error(error.message ?: "Failed to update status")
                }
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            deleteTaskUseCase(id)
                .onSuccess {
                    loadStatistics()
                }
                .onFailure { error ->
                    _uiState.value = TaskListUiState.Error(error.message ?: "Failed to delete task")
                }
        }
    }

    fun onCleared() {
        viewModelScope.coroutineContext[Job]?.cancel()
    }
}