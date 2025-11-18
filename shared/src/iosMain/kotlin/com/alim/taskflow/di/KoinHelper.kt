package com.alim.taskflow.di

import com.alim.taskflow.domain.usecase.*
import com.alim.taskflow.presentation.TaskDetailViewModel
import com.alim.taskflow.presentation.TaskListViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.text.contains

/**
 * Helper class for iOS to access Koin dependencies
 */
class KoinHelper : KoinComponent {

    companion object {
        private var isInitialized = false

        fun ensureKoinStarted() {
            if (!isInitialized) {
                try {
                    initKoin()
                    isInitialized = true
                    println("✅ Koin initialized successfully for iOS")
                } catch (e: Exception) {
                    if (e.message?.contains("A Koin Application has already been started") == true) {
                        println("ℹ️ Koin already initialized")
                        isInitialized = true
                    } else {
                        println("❌ Error initializing Koin: ${e.message}")
                        e.printStackTrace()
                        throw e
                    }
                }
            } else {
                println("ℹ️ Koin already initialized")
            }
        }
    }

    init {
        ensureKoinStarted()
    }

    fun createTaskListViewModel(): TaskListViewModel {
        val getAllTasksUseCase: GetAllTasksUseCase by inject()
        val addTaskUseCase: AddTaskUseCase by inject()
        val updateTaskUseCase: UpdateTaskUseCase by inject()
        val deleteTaskUseCase: DeleteTaskUseCase by inject()
        val searchTasksUseCase: SearchTasksUseCase by inject()
        val filterTasksUseCase: FilterTasksUseCase by inject()
        val getTaskStatisticsUseCase: GetTaskStatisticsUseCase by inject()

        return TaskListViewModel(
            getAllTasksUseCase,
            addTaskUseCase,
            updateTaskUseCase,
            deleteTaskUseCase,
            searchTasksUseCase,
            filterTasksUseCase,
            getTaskStatisticsUseCase
        )
    }

    fun createTaskDetailViewModel(): TaskDetailViewModel {
        val getTaskByIdUseCase: GetTaskByIdUseCase by inject()
        return TaskDetailViewModel(getTaskByIdUseCase)
    }
}