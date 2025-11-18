package com.alim.taskflow.core.navigation

import com.alim.taskflow.features.addTask.AddTaskScreen
import com.alim.taskflow.features.taskDetails.TaskDetailScreen
import com.alim.taskflow.features.editTask.EditTaskScreen
import com.alim.taskflow.features.filterTask.FilterScreen
import com.alim.taskflow.features.main.MainScreen
import com.alim.taskflow.features.splash.SplashScreen

object ScreenFactory {

    @Suppress("UNCHECKED_CAST")
    fun createScreen(appScreen: AppScreens): ActivityScreen<AppScreens> {
        return when(appScreen) {
            AppScreens.Splash -> SplashScreen() as ActivityScreen<AppScreens>
            AppScreens.Main -> MainScreen() as ActivityScreen<AppScreens>
            AppScreens.FilterTask -> FilterScreen() as ActivityScreen<AppScreens>
            AppScreens.AddTask -> AddTaskScreen() as ActivityScreen<AppScreens>
            is AppScreens.TaskDetails -> TaskDetailScreen() as ActivityScreen<AppScreens>
            is AppScreens.EditTask -> EditTaskScreen() as ActivityScreen<AppScreens>
        }
    }
}