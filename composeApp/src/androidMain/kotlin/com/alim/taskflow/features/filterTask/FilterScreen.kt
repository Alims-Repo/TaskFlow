package com.alim.taskflow.features.filterTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alim.taskflow.core.navigation.ActivityScreen
import com.alim.taskflow.core.navigation.AppScreens
import com.alim.taskflow.core.navigation.NavController
import com.alim.taskflow.core.theme.Blue
import com.alim.taskflow.core.theme.PriorityHigh
import com.alim.taskflow.core.theme.PriorityLow
import com.alim.taskflow.core.theme.PriorityMedium
import com.alim.taskflow.core.theme.Red
import com.alim.taskflow.core.theme.Spacing
import com.alim.taskflow.core.theme.StatusDone
import com.alim.taskflow.core.theme.StatusInProgress
import com.alim.taskflow.core.theme.StatusTodo
import com.alim.taskflow.domain.model.Priority
import com.alim.taskflow.domain.model.SortOption
import com.alim.taskflow.domain.model.TaskStatus
import com.alim.taskflow.features.filterTask.components.FilterToggleItem
import com.alim.taskflow.features.filterTask.utils.getSortOptionDisplayName
import com.alim.taskflow.features.filterTask.utils.getSortOptionIcon
import com.alim.taskflow.presentation.TaskListViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class FilterScreen : ActivityScreen<AppScreens.FilterTask>() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun InitView(
        navKey: AppScreens.FilterTask,
        navController: NavController
    ) {
        val viewModel: TaskListViewModel = koinViewModel()

        var selectedSortOption by remember { mutableStateOf(SortOption.DATE_CREATED_DESC) }
        var selectedStatuses by remember { mutableStateOf(setOf<TaskStatus>()) }
        var selectedPriorities by remember { mutableStateOf(setOf<Priority>()) }
        var showOverdueOnly by remember { mutableStateOf(false) }
        var expandedSortMenu by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Filters & Sort") },
                    navigationIcon = {
                        IconButton(onClick = navController::back) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                viewModel.setSortOption(selectedSortOption)
                                viewModel.applyFilters(
                                    statuses = selectedStatuses,
                                    priorities = selectedPriorities,
                                    showOverdueOnly = showOverdueOnly
                                )
                                navController.back()
                            }
                        ) {
                            Text("Apply")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
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
                                Blue.copy(alpha = 0.03f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(Spacing.l),
                    verticalArrangement = Arrangement.spacedBy(Spacing.l)
                ) {
                    // Sort Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.l),
                            verticalArrangement = Arrangement.spacedBy(Spacing.m)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Sort By",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            ExposedDropdownMenuBox(
                                expanded = expandedSortMenu,
                                onExpandedChange = { expandedSortMenu = it }
                            ) {
                                TextField(
                                    value = getSortOptionDisplayName(selectedSortOption),
                                    onValueChange = {},
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSortMenu)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                ExposedDropdownMenu(
                                    expanded = expandedSortMenu,
                                    onDismissRequest = { expandedSortMenu = false }
                                ) {
                                    SortOption.values().forEach { option ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(
                                                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = getSortOptionIcon(option),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Text(getSortOptionDisplayName(option))
                                                }
                                            },
                                            onClick = {
                                                selectedSortOption = option
                                                expandedSortMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Status Filters Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.l),
                            verticalArrangement = Arrangement.spacedBy(Spacing.m)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Circle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Filter by Status",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            FilterToggleItem(
                                title = "To Do",
                                icon = Icons.Default.Circle,
                                color = StatusTodo,
                                isChecked = selectedStatuses.contains(TaskStatus.TODO),
                                onCheckedChange = { checked ->
                                    selectedStatuses = if (checked) {
                                        selectedStatuses + TaskStatus.TODO
                                    } else {
                                        selectedStatuses - TaskStatus.TODO
                                    }
                                }
                            )

                            FilterToggleItem(
                                title = "In Progress",
                                icon = Icons.Default.AccessTime,
                                color = StatusInProgress,
                                isChecked = selectedStatuses.contains(TaskStatus.IN_PROGRESS),
                                onCheckedChange = { checked ->
                                    selectedStatuses = if (checked) {
                                        selectedStatuses + TaskStatus.IN_PROGRESS
                                    } else {
                                        selectedStatuses - TaskStatus.IN_PROGRESS
                                    }
                                }
                            )

                            FilterToggleItem(
                                title = "Completed",
                                icon = Icons.Default.CheckCircle,
                                color = StatusDone,
                                isChecked = selectedStatuses.contains(TaskStatus.DONE),
                                onCheckedChange = { checked ->
                                    selectedStatuses = if (checked) {
                                        selectedStatuses + TaskStatus.DONE
                                    } else {
                                        selectedStatuses - TaskStatus.DONE
                                    }
                                }
                            )
                        }
                    }

                    // Priority Filters Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.l),
                            verticalArrangement = Arrangement.spacedBy(Spacing.m)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Flag,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Filter by Priority",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            FilterToggleItem(
                                title = "Low",
                                icon = Icons.Default.Flag,
                                color = PriorityLow,
                                isChecked = selectedPriorities.contains(Priority.LOW),
                                onCheckedChange = { checked ->
                                    selectedPriorities = if (checked) {
                                        selectedPriorities + Priority.LOW
                                    } else {
                                        selectedPriorities - Priority.LOW
                                    }
                                }
                            )

                            FilterToggleItem(
                                title = "Medium",
                                icon = Icons.Default.Flag,
                                color = PriorityMedium,
                                isChecked = selectedPriorities.contains(Priority.MEDIUM),
                                onCheckedChange = { checked ->
                                    selectedPriorities = if (checked) {
                                        selectedPriorities + Priority.MEDIUM
                                    } else {
                                        selectedPriorities - Priority.MEDIUM
                                    }
                                }
                            )

                            FilterToggleItem(
                                title = "High",
                                icon = Icons.Default.Flag,
                                color = PriorityHigh,
                                isChecked = selectedPriorities.contains(Priority.HIGH),
                                onCheckedChange = { checked ->
                                    selectedPriorities = if (checked) {
                                        selectedPriorities + Priority.HIGH
                                    } else {
                                        selectedPriorities - Priority.HIGH
                                    }
                                }
                            )
                        }
                    }

                    // Other Filters Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Spacing.l),
                            verticalArrangement = Arrangement.spacedBy(Spacing.m)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Other Filters",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            FilterToggleItem(
                                title = "Show Overdue Only",
                                icon = Icons.Default.Warning,
                                color = Red,
                                isChecked = showOverdueOnly,
                                onCheckedChange = { showOverdueOnly = it }
                            )
                        }
                    }

                    // Clear All Button
                    Button(
                        onClick = {
                            selectedSortOption = SortOption.DATE_CREATED_DESC
                            selectedStatuses = setOf()
                            selectedPriorities = setOf()
                            showOverdueOnly = false
                            viewModel.clearFilters()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Red
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(Spacing.s))
                        Text("Clear All Filters")
                    }
                }
            }
        }
    }
}