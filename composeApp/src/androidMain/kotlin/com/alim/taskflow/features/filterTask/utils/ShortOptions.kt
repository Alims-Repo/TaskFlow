package com.alim.taskflow.features.filterTask.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.TextFields
import com.alim.taskflow.domain.model.SortOption

fun getSortOptionDisplayName(option: SortOption): String {
    return when (option) {
        SortOption.DATE_CREATED_DESC -> "Date Created (Newest First)"
        SortOption.DATE_CREATED_ASC -> "Date Created (Oldest First)"
        SortOption.DUE_DATE_ASC -> "Due Date (Nearest First)"
        SortOption.DUE_DATE_DESC -> "Due Date (Farthest First)"
        SortOption.PRIORITY_HIGH_TO_LOW -> "Priority (High to Low)"
        SortOption.PRIORITY_LOW_TO_HIGH -> "Priority (Low to High)"
        SortOption.TITLE_A_TO_Z -> "Title (A to Z)"
        SortOption.TITLE_Z_TO_A -> "Title (Z to A)"
    }
}

fun getSortOptionIcon(option: SortOption): androidx.compose.ui.graphics.vector.ImageVector {
    return when (option) {
        SortOption.DATE_CREATED_DESC, SortOption.DATE_CREATED_ASC -> Icons.Default.CalendarToday
        SortOption.DUE_DATE_ASC, SortOption.DUE_DATE_DESC -> Icons.Default.CalendarMonth
        SortOption.PRIORITY_HIGH_TO_LOW, SortOption.PRIORITY_LOW_TO_HIGH -> Icons.Default.Flag
        SortOption.TITLE_A_TO_Z, SortOption.TITLE_Z_TO_A -> Icons.Default.TextFields
    }
}