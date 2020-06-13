package com.example.vendingmachine.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.vendingmachine.data.TaskDatabase

class DailyHabitReset(context: Context, params : WorkerParameters) : Worker(context, params){

    override fun doWork(): Result {
        val appContext = applicationContext

        return try{
            val datasource = TaskDatabase.getInstance(appContext)

            val checkedHabits = datasource.taskDao.getCompleteHabits()

            checkedHabits.forEach {it ->
                it.isCompleted = false
                datasource.taskDao.updateTask(it)
            }
            return Result.success()
        }catch (throwable : Throwable){
            Log.e("Habit reset", "Couldn't access database", throwable)
            Result.failure()
        }
    }

}