package com.alim.taskflow.di

import com.alim.taskflow.data.local.database.TaskFlowDatabase
import com.alim.taskflow.data.local.database.dao.TaskDao
import com.alim.taskflow.data.repository.TaskRepositoryImpl
import com.alim.taskflow.domain.repository.TaskRepository
import com.alim.taskflow.domain.usecase.*
import com.alim.taskflow.platform.getDatabaseBuilder
import com.alim.taskflow.presentation.TaskDetailViewModel
import com.alim.taskflow.presentation.TaskListViewModel
import org.koin.dsl.module
import org.koin.core.module.Module

expect fun platformModule(): Module

fun databaseModule() = module {
    single<TaskFlowDatabase> { getDatabaseBuilder().build() }
    single<TaskDao> { get<TaskFlowDatabase>().taskDao() }
}

val repositoryModule = module {
    single<TaskRepository> {
        TaskRepositoryImpl(get())
    }
}

val useCaseModule = module {
    factory { GetAllTasksUseCase(get()) }
    factory { GetTaskByIdUseCase(get()) }
    factory { AddTaskUseCase(get()) }
    factory { UpdateTaskUseCase(get()) }
    factory { DeleteTaskUseCase(get()) }
    factory { SearchTasksUseCase(get()) }
    factory { FilterTasksUseCase(get()) }
    factory { GetTaskStatisticsUseCase(get()) }
}