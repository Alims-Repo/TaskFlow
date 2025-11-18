package com.alim.taskflow.di

import org.koin.core.module.Module
import org.koin.dsl.module
import com.alim.taskflow.presentation.TaskDetailViewModel
import com.alim.taskflow.presentation.TaskListViewModel

actual fun platformModule() = module {
    factory { TaskListViewModel(get(), get(), get(), get(), get(), get(), get()) }
    factory { TaskDetailViewModel(get()) }
}