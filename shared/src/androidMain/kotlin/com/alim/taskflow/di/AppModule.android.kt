package com.alim.taskflow.di

import com.alim.taskflow.presentation.TaskDetailViewModel
import com.alim.taskflow.presentation.TaskListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf

actual fun platformModule() = module {
    viewModelOf(::TaskListViewModel)
    viewModelOf(::TaskDetailViewModel)
}