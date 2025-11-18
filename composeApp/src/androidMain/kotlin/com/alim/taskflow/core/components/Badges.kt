package com.alim.taskflow.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alim.taskflow.core.theme.PriorityHigh
import com.alim.taskflow.core.theme.PriorityLow
import com.alim.taskflow.core.theme.PriorityMedium
import com.alim.taskflow.core.theme.Red
import com.alim.taskflow.core.theme.StatusDone
import com.alim.taskflow.core.theme.StatusInProgress
import com.alim.taskflow.core.theme.StatusTodo
import com.alim.taskflow.domain.model.Priority
import com.alim.taskflow.domain.model.TaskStatus
import kotlinx.datetime.Instant
import kotlinx.datetime.Clock

@Composable
fun PriorityBadge(priority: Priority, modifier: Modifier = Modifier) {
    val color = when (priority) {
        Priority.LOW -> PriorityLow
        Priority.MEDIUM -> PriorityMedium
        Priority.HIGH -> PriorityHigh
    }

    val text = when (priority) {
        Priority.LOW -> "Low"
        Priority.MEDIUM -> "Medium"
        Priority.HIGH -> "High"
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}

@Composable
fun StatusBadge(status: TaskStatus, modifier: Modifier = Modifier) {
    val color = when (status) {
        TaskStatus.TODO -> StatusTodo
        TaskStatus.IN_PROGRESS -> StatusInProgress
        TaskStatus.DONE -> StatusDone
    }

    Box(
        modifier = modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Composable
fun DueDateBadge(dueDate: Instant, modifier: Modifier = Modifier) {
    val now = Clock.System.now()
    val isOverdue = dueDate < now

    val color = if (isOverdue) Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val icon = if (isOverdue) Icons.Default.Warning else Icons.Default.CalendarToday

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isOverdue) Red.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
            tint = color
        )

        Text(
            text = formatDate(dueDate),
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}

private fun formatDate(instant: Instant): String {
    // Simple formatting - you can enhance this
    val date = instant.toString().substringBefore('T')
    return date
}