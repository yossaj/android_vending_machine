package com.example.vendingmachine.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vendingmachine.data.models.Task

@Database(entities = [Task::class], version = 5, exportSchema = false)
abstract class TaskDatabase : RoomDatabase(){
    abstract fun getTaskDao() : TaskDao

    companion object{
        const val DATABASE_NAME = "task_database"
    }
}