package com.alim.taskflow.features.taskDetails.components

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun formatDateFromMillis(millis: Long): String {
    val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(millis)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return "${localDate.year}-${localDate.monthNumber.toString().padStart(2, '0')}-${localDate.dayOfMonth.toString().padStart(2, '0')}"
}