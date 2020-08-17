package com.example.vendingmachine.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 4, exportSchema = false)
abstract class TaskDatabase : RoomDatabase(){
    abstract fun getTaskDao() : TaskDao

    companion object{
        const val DATABASE_NAME = "task_database"
    }
}