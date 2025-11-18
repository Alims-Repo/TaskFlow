package com.alim.taskflow.features.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.alim.taskflow.core.navigation.ActivityScreen
import com.alim.taskflow.core.navigation.AppScreens
import com.alim.taskflow.core.navigation.NavController
import com.alim.taskflow.core.theme.Blue
import com.alim.taskflow.core.theme.Spacing
import com.alim.taskflow.features.main.components.SearchBar
import com.alim.taskflow.presentation.TaskListUiState
import com.alim.taskflow.presentation.TaskListViewModel
import com.alim.taskflow.core.components.EmptyState
import com.alim.taskflow.core.components.ErrorState
import com.alim.taskflow.core.components.LoadingState
import com.alim.taskflow.core.components.StatisticsCard
import com.alim.taskflow.core.components.TaskCard
import org.koin.compose.koinInject

class MainScreen : ActivityScreen<AppScreens.Main>() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun InitView(
        navKey: AppScreens.Main,
        navController: NavController
    ) {

        val viewModel: TaskListViewModel = koinInject()

        val uiState by viewModel.uiState.collectAsState()
        val statistics by viewModel.statistics.collectAsState()
        var searchQuery by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "TaskFlow",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(AppScreens.FilterTask) }) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filter"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(AppScreens.AddTask) },
                    containerColor = Blue,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Task"
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                Blue.copy(alpha = 0.05f)
                            )
                        )
                    )
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(Spacing.l),
                    verticalArrangement = Arrangement.spacedBy(Spacing.m)
                ) {
                    item {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { query ->
                                searchQuery = query
                                viewModel.searchTasks(query)
                            }
                        )
                    }

                    when (val state = uiState) {
                        is TaskListUiState.Loading -> {
                            item {
                                LoadingState()
                            }
                        }
                        is TaskListUiState.Error -> {
                            item {
                                ErrorState(
                                    message = state.message,
                                    onRetry = { /* Retry logic */ }
                                )
                            }
                        }
                        is TaskListUiState.Success -> {
                            if (state.tasks.isEmpty() && searchQuery.isEmpty()) {
                                item {
                                    EmptyState()
                                }
                            } else {
                                statistics?.let { stats ->
                                    item {
                                        StatisticsCard(statistics = stats)
                                    }
                                }

                                // Tasks Section
                                if (state.tasks.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Tasks",
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier.padding(vertical = Spacing.s)
                                        )
                                    }

                                    items(
                                        items = state.tasks,
                                        key = { it.id }
                                    ) { task ->
                                        TaskCard(
                                            task = task,
                                            onTaskClick = { navController.navigate(AppScreens.TaskDetails(task.id)) },
                                            onStatusChange = { newStatus ->
                                                viewModel.updateTaskStatus(task.id, newStatus)
                                            }
                                        )
                                    }
                                } else if (searchQuery.isNotEmpty()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 32.dp),
                                            contentAlignment = androidx.compose.ui.Alignment.Center
                                        ) {
                                            Text(
                                                text = "No results found",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }
            }
        }
    }
}