package com.alim.taskflow.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.alim.taskflow.data.local.database.dao.TaskDao
import com.alim.taskflow.data.local.entity.TaskEntity
import com.alim.taskflow.platform.TaskDatabaseConstructor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(TaskDatabaseConstructor::class)
abstract class TaskFlowDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

//fun getRoomDatabase(
//    builder: RoomDatabase.Builder<TaskFlowDatabase>
//): TaskFlowDatabase {
//    return builder
//        .addMigrations()
//        .fallbackToDestructiveMigrationOnDowngrade(true)
//        .setDriver(BundledSQLiteDriver())
//        .setQueryCoroutineContext(Dispatchers.IO)
//        .build()
//}
//
//fun getUserGrowthDao(taskFlowDatabase: TaskFlowDatabase) = taskFlowDatabase.taskDao()