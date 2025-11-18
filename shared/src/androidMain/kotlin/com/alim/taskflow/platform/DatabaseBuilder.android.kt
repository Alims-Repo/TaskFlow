package com.alim.taskflow.platform

import androidx.room.RoomDatabase
import com.alim.taskflow.data.local.database.TaskFlowDatabase
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import org.koin.mp.KoinPlatform.getKoin

actual fun getDatabaseBuilder(): RoomDatabase.Builder<TaskFlowDatabase> {
    val context = getKoin().get<Context>()
    val dbFile = context.getDatabasePath("task_flow_database.db")
    return Room.databaseBuilder<TaskFlowDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    )
}