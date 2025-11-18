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

    // For iOS compatibility - manual cleanup
    fun onCleared() {
        viewModelScope.coroutineContext[Job]?.cancel()
    }

    private fun insertDummyData() {
        viewModelScope.launch {
            val now = Clock.System.now()

            // Task 1: High priority overdue task
            addTaskUseCase(
                title = "Fix critical bug in production",
                description = "Users are reporting app crashes on Android 14. Needs immediate attention.",
                priority = Priority.HIGH,
                status = TaskStatus.IN_PROGRESS,
                dueDate = now.minus(2.days)
            )

            // Task 2: Medium priority today's task
            addTaskUseCase(
                title = "Review pull requests",
                description = "Review and merge pending PRs from team members.",
                priority = Priority.MEDIUM,
                status = TaskStatus.TODO,
                dueDate = now
            )

            // Task 3: Low priority future task
            addTaskUseCase(
                title = "Update project documentation",
                description = "Add API documentation and code examples for new features.",
                priority = Priority.LOW,
                status = TaskStatus.TODO,
                dueDate = now.plus(7.days)
            )

            // Task 4: High priority urgent task
            addTaskUseCase(
                title = "Prepare presentation for stakeholders",
                description = "Create slides showcasing Q4 progress and goals.",
                priority = Priority.HIGH,
                status = TaskStatus.TODO,
                dueDate = now.plus(1.days)
            )

            // Task 5: Completed task
            addTaskUseCase(
                title = "Setup CI/CD pipeline",
                description = "Configure GitHub Actions for automated testing and deployment.",
                priority = Priority.HIGH,
                status = TaskStatus.DONE,
                dueDate = now.minus(5.days)
            )

            // Task 6: Medium priority task
            addTaskUseCase(
                title = "Optimize database queries",
                description = "Improve performance of slow queries in the dashboard.",
                priority = Priority.MEDIUM,
                status = TaskStatus.IN_PROGRESS,
                dueDate = now.plus(3.days)
            )

            // Task 7: Low priority task without due date
            addTaskUseCase(
                title = "Refactor legacy code",
                description = "Clean up old authentication module.",
                priority = Priority.LOW,
                status = TaskStatus.TODO,
                dueDate = null
            )

            // Task 8: High priority task
            addTaskUseCase(
                title = "Security audit",
                description = "Conduct security review of API endpoints and implement fixes.",
                priority = Priority.HIGH,
                status = TaskStatus.TODO,
                dueDate = now.plus(5.days)
            )

            // Task 9: Medium priority completed task
            addTaskUseCase(
                title = "Design new onboarding flow",
                description = "Create wireframes and user flow for improved user onboarding.",
                priority = Priority.MEDIUM,
                status = TaskStatus.DONE,
                dueDate = now.minus(10.days)
            )

            // Task 10: Low priority task
            addTaskUseCase(
                title = "Research new libraries",
                description = "Evaluate potential libraries for state management and networking.",
                priority = Priority.LOW,
                status = TaskStatus.TODO,
                dueDate = now.plus(14.days)
            )
        }
    }

    init {
        getAllTasksUseCase().onEach {
            if (it.isEmpty()) insertDummyData()
            else it.forEach {
                println(it.title)
            }
        }.launchIn(viewModelScope)
    }
}