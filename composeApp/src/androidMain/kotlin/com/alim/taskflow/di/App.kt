package com.alim.taskflow.di

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import com.alim.taskflow.core.animation.Animations.popTransitionSpec
import com.alim.taskflow.core.animation.Animations.transitionSpec
import com.alim.taskflow.core.navigation.AppScreens
import com.alim.taskflow.core.navigation.NavController
import com.alim.taskflow.core.navigation.ScreenFactory
import com.alim.taskflow.core.theme.TaskFlowTheme

@Composable
fun App() {
    TaskFlowTheme {

        val applicationStack = rememberNavBackStack(AppScreens.Splash)
        val navController = remember { NavController(applicationStack) }

        NavDisplay(
            modifier = Modifier.fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.background
                ),
            backStack = applicationStack,
            transitionSpec = { transitionSpec },
            popTransitionSpec = { popTransitionSpec },
        ) { route ->
            NavEntry(route) {
                val animatedContentScope = LocalNavAnimatedContentScope.current
                ScreenFactory.createScreen(route as AppScreens).InitView(
                    navKey = route,
                    navController = navController
                )
            }
        }
    }
}