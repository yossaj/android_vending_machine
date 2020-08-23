package com.example.vendingmachine.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vendingmachine.data.models.Habit
import com.example.vendingmachine.data.models.Task
import com.example.vendingmachine.data.persistence.TaskDao

@Database(entities = [Task::class, Habit::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase(){
    abstract fun getTaskDao() : TaskDao
    abstract fun getHabitDao() : HabitDao

    companion object{
        const val DATABASE_NAME = "task_database"
    }
}