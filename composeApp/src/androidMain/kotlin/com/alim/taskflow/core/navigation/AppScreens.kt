package com.alim.taskflow.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class AppScreens : NavKey {

    @Serializable
    data object Splash: AppScreens()

    @Serializable
    data object Main: AppScreens()

    @Serializable
    data object AddTask: AppScreens()

    @Serializable
    data class TaskDetails(val id: String): AppScreens()

    @Serializable
    data class EditTask(val id: String): AppScreens()

    @Serializable
    data object FilterTask: AppScreens()
}