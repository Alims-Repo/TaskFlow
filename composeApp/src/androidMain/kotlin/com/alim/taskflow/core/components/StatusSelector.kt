package com.alim.taskflow.core.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.alim.taskflow.core.theme.StatusDone
import com.alim.taskflow.core.theme.StatusInProgress
import com.alim.taskflow.core.theme.StatusTodo
import com.alim.taskflow.domain.model.TaskStatus

@Composable
fun StatusSelector(
    currentStatus: TaskStatus,
    onStatusChange: (TaskStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(vertical = 6.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatusButton(
            status = TaskStatus.TODO,
            isSelected = currentStatus == TaskStatus.TODO,
            icon = Icons.Default.Circle,
            selectedIcon = Icons.Default.Circle,
            onClick = { onStatusChange(TaskStatus.TODO) }
        )

        StatusButton(
            status = TaskStatus.IN_PROGRESS,
            isSelected = currentStatus == TaskStatus.IN_PROGRESS,
            icon = Icons.Default.AccessTime,
            selectedIcon = Icons.Default.AccessTime,
            onClick = { onStatusChange(TaskStatus.IN_PROGRESS) }
        )

        StatusButton(
            status = TaskStatus.DONE,
            isSelected = currentStatus == TaskStatus.DONE,
            icon = Icons.Default.CheckCircleOutline,
            selectedIcon = Icons.Default.CheckCircle,
            onClick = { onStatusChange(TaskStatus.DONE) }
        )
    }
}

@Composable
private fun StatusButton(
    status: TaskStatus,
    isSelected: Boolean,
    icon: ImageVector,
    selectedIcon: ImageVector,
    onClick: () -> Unit
) {
    val color = when (status) {
        TaskStatus.TODO -> StatusTodo
        TaskStatus.IN_PROGRESS -> StatusInProgress
        TaskStatus.DONE -> StatusDone
    }

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    val iconAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.4f,
        label = "alpha"
    )

    Icon(
        imageVector = if (isSelected) selectedIcon else icon,
        contentDescription = null,
        modifier = Modifier
            .size(if (isSelected) 20.dp else 17.dp)
            .scale(scale)
            .clickable(onClick = onClick),
        tint = color.copy(alpha = iconAlpha)
    )
}