package com.alim.taskflow.platform

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.alim.taskflow.data.local.database.TaskFlowDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<TaskFlowDatabase>


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object TaskDatabaseConstructor : RoomDatabaseConstructor<TaskFlowDatabase> {
    override fun initialize(): TaskFlowDatabase
}