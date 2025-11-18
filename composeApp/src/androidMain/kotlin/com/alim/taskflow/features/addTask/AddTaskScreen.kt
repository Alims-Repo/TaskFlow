package com.alim.taskflow.features.addTask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
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
import com.alim.taskflow.core.theme.Spacing
import com.alim.taskflow.domain.model.Priority
import com.alim.taskflow.domain.model.TaskStatus
import com.alim.taskflow.features.addTask.components.PrioritySelectionButton
import com.alim.taskflow.features.addTask.components.StatusSelectionButton
import com.alim.taskflow.features.taskDetails.components.formatDateFromMillis
import com.alim.taskflow.presentation.TaskListViewModel
import org.koin.compose.koinInject
import kotlin.text.ifEmpty

class AddTaskScreen : ActivityScreen<AppScreens.AddTask>() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun InitView(
        navKey: AppScreens.AddTask,
        navController: NavController
    ) {
        val viewModel: TaskListViewModel = koinInject()

        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var priority by remember { mutableStateOf(Priority.MEDIUM) }
        var status by remember { mutableStateOf(TaskStatus.TODO) }
        var hasDueDate by remember { mutableStateOf(false) }
        var showDatePicker by remember { mutableStateOf(false) }
        var showTimePicker by remember { mutableStateOf(false) }
        var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
        var selectedHour by remember { mutableStateOf(12) }
        var selectedMinute by remember { mutableStateOf(0) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("New Task") },
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
                                val dueDate = if (hasDueDate && selectedDateMillis != null) {
                                    // Convert to Instant
                                    kotlinx.datetime.Instant.fromEpochMilliseconds(selectedDateMillis!!)
                                } else null

                                viewModel.addTask(
                                    title = title,
                                    description = description.ifEmpty { null },
                                    priority = priority,
                                    status = status,
                                    dueDate = dueDate
                                )
                                navController.back()
                            },
                            enabled = title.isNotBlank()
                        ) {
                            Text("Add")
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
                    // Title & Description Card
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
                            // Title Section
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TextFields,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Title",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }

                                TextField(
                                    value = title,
                                    onValueChange = { title = it },
                                    placeholder = { Text("Enter task title") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }

                            // Description Section
                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Description",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }

                                TextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    placeholder = { Text("Add details (optional)") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 100.dp),
                                    minLines = 3,
                                    maxLines = 6,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    ),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }

                    // Priority Card
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
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Priority",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(Spacing.m)
                            ) {
                                PrioritySelectionButton(
                                    priority = Priority.LOW,
                                    isSelected = priority == Priority.LOW,
                                    onClick = { priority = Priority.LOW },
                                    modifier = Modifier.weight(1f)
                                )

                                PrioritySelectionButton(
                                    priority = Priority.MEDIUM,
                                    isSelected = priority == Priority.MEDIUM,
                                    onClick = { priority = Priority.MEDIUM },
                                    modifier = Modifier.weight(1f)
                                )

                                PrioritySelectionButton(
                                    priority = Priority.HIGH,
                                    isSelected = priority == Priority.HIGH,
                                    onClick = { priority = Priority.HIGH },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Status Card
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
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Status",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(Spacing.s)) {
                                StatusSelectionButton(
                                    status = TaskStatus.TODO,
                                    isSelected = status == TaskStatus.TODO,
                                    onClick = { status = TaskStatus.TODO }
                                )

                                StatusSelectionButton(
                                    status = TaskStatus.IN_PROGRESS,
                                    isSelected = status == TaskStatus.IN_PROGRESS,
                                    onClick = { status = TaskStatus.IN_PROGRESS }
                                )

                                StatusSelectionButton(
                                    status = TaskStatus.DONE,
                                    isSelected = status == TaskStatus.DONE,
                                    onClick = { status = TaskStatus.DONE }
                                )
                            }
                        }
                    }

                    // Due Date Card
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
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(Spacing.s),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Set Due Date",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }

                                Switch(
                                    checked = hasDueDate,
                                    onCheckedChange = { hasDueDate = it },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Blue
                                    )
                                )
                            }

                            if (hasDueDate) {
                                Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
                                    Button(
                                        onClick = { showDatePicker = true },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(Spacing.s))
                                        Text(
                                            text = if (selectedDateMillis != null) {
                                                formatDateFromMillis(selectedDateMillis!!)
                                            } else {
                                                "Select Date"
                                            },
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Button(
                                        onClick = { showTimePicker = true },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(Spacing.s))
                                        Text(
                                            text = String.format("%02d:%02d", selectedHour, selectedMinute),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedDateMillis = datePickerState.selectedDateMillis
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        // Time Picker Dialog
        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = selectedHour,
                initialMinute = selectedMinute
            )
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedHour = timePickerState.hour
                            selectedMinute = timePickerState.minute
                            showTimePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text("Cancel")
                    }
                },
                text = {
                    TimePicker(state = timePickerState)
                }
            )
        }
    }
}