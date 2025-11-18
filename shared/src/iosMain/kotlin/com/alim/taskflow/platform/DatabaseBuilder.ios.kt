package com.alim.taskflow.platform

import androidx.room.RoomDatabase
import com.alim.taskflow.data.local.database.TaskFlowDatabase
import androidx.room.Room
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual fun getDatabaseBuilder(): RoomDatabase.Builder<TaskFlowDatabase> {
    val dbFilePath = documentDirectory() + "/task_flow_database.db"
    println("iOS Database path: $dbFilePath")
    return Room.databaseBuilder<TaskFlowDatabase>(
        name = dbFilePath,
        factory = { TaskDatabaseConstructor.initialize() }
    ).setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    println("documentDirectory -> Entered")
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}