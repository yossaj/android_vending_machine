package com.example.vendingmachine.workers

import android.content.Context
import android.util.Log
import android.util.TimeUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.vendingmachine.data.TaskDatabase
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class DailyHabitReset(context: Context, params : WorkerParameters) : Worker(context, params){

    override fun doWork(): Result {
        val appContext = applicationContext

        return try{
            val datasource = TaskDatabase.getInstance(appContext)

            val checkedHabits = datasource.taskDao.getCompleteHabits()

            val currentTime = System.currentTimeMillis()

            checkedHabits.forEach {it ->
                val uncheckDely = TimeUnit.HOURS.toMillis(20)
                val unchecktime = (it.updatedTime + uncheckDely )
                if(currentTime >= unchecktime) {
                    it.isCompleted = false
                    datasource.taskDao.updateTask(it)
                }
            }
            return Result.success()
        }catch (throwable : Throwable){
            Log.e("Habit reset", "Couldn't access database", throwable)
            Result.failure()
        }
    }

}