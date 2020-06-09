package com.example.vendingmachine.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 2, exportSchema = false)
abstract class TaskDatabase : RoomDatabase(){
    abstract val taskDao : TaskDao

    companion object{

        @Volatile
        private var INSTANCE: TaskDatabase? = null
        private const val TABLE_NAME = "task_database"

        fun getInstance(context : Context) : TaskDatabase{
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        TABLE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}