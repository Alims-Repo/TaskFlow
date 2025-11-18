package com.alim.taskflow.core.navigation

import androidx.compose.runtime.Composable

abstract class ActivityScreen<T> {

    @Composable
    abstract fun InitView(
        navKey: T,
        navController: NavController
    )
}