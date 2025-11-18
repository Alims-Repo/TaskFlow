package com.alim.taskflow.features.taskDetails

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.alim.taskflow.core.theme.Green
import com.alim.taskflow.core.theme.Orange
import com.alim.taskflow.core.theme.Red
import com.alim.taskflow.core.theme.Spacing
import com.alim.taskflow.domain.model.TaskStatus
import com.alim.taskflow.features.taskDetails.components.DetailRow
import com.alim.taskflow.features.taskDetails.components.formatInstant
import com.alim.taskflow.presentation.TaskListUiState
import com.alim.taskflow.presentation.TaskListViewModel
import com.alim.taskflow.core.components.PriorityBadge
import com.alim.taskflow.core.components.StatusBadge
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class TaskDetailScreen : ActivityScreen<AppScreens.TaskDetails>() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun InitView(
        navKey: AppScreens.TaskDetails,
        navController: NavController
    ) {
        val viewModel: TaskListViewModel = koinViewModel()
        val uiState by viewModel.uiState.collectAsState()
        var showDeleteDialog by remember { mutableStateOf(false) }

        val task = when (val state = uiState) {
            is TaskListUiState.Success -> state.tasks.find { it.id == navKey.id }
            else -> null
        }

        if (task == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Task Details") },
                    navigationIcon = {
                        IconButton(onClick = navController::back) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
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
                    verticalArrangement = Arrangement.spacedBy(Spacing.xxl)
                ) {
                    // Header Card
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
                            verticalArrangement = Arrangement.spacedBy(Spacing.l)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(Spacing.s)
                                ) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        StatusBadge(status = task.status)
                                        PriorityBadge(priority = task.priority)
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        val newStatus = if (task.status == TaskStatus.DONE) {
                                            TaskStatus.TODO
                                        } else {
                                            TaskStatus.DONE
                                        }
                                        viewModel.updateTaskStatus(navKey.id, newStatus)
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (task.status == TaskStatus.DONE) {
                                            Icons.Default.CheckCircle
                                        } else {
                                            Icons.Default.Circle
                                        },
                                        contentDescription = "Toggle completion",
                                        tint = if (task.status == TaskStatus.DONE) {
                                            Green
                                        } else {
                                            Color.Gray
                                        },
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Description Card
                    task.description?.let { description ->
                        if (description.isNotEmpty()) {
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
                                            imageVector = Icons.Default.Description,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Description",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Text(
                                        text = description,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                                    )
                                }
                            }
                        }
                    }

                    // Details Card
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
                            verticalArrangement = Arrangement.spacedBy(Spacing.l)
                        ) {
                            // Created Date
                            DetailRow(
                                icon = Icons.Default.CalendarMonth,
                                label = "Created",
                                value = formatInstant(task.createdAt),
                                color = Blue
                            )

                            Divider()

                            // Due Date
                            task.dueDate?.let { dueDate ->
                                DetailRow(
                                    icon = Icons.Default.CalendarToday,
                                    label = "Due Date",
                                    value = formatInstant(dueDate),
                                    color = if (dueDate < kotlinx.datetime.Clock.System.now()) {
                                        Red
                                    } else {
                                        Orange
                                    }
                                )
                            } ?: run {
                                DetailRow(
                                    icon = Icons.Default.CalendarToday,
                                    label = "Due Date",
                                    value = "Not set",
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // Action Buttons
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing.m)
                    ) {
                        Button(
                            onClick = { navController.navigate(AppScreens.EditTask(navKey.id)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Blue
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(Spacing.s))
                            Text("Edit Task")
                        }

                        Button(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Red
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(Spacing.s))
                            Text("Delete Task")
                        }
                    }
                }
            }
        }

        // Delete Confirmation Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Task") },
                text = {
                    Text("Are you sure you want to delete this task? This action cannot be undone.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteTask(navKey.id)
                            showDeleteDialog = false
                            navController.back()
                        }
                    ) {
                        Text("Delete", color = Red)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}