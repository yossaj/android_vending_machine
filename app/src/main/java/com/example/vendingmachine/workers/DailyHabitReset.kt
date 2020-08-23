package com.example.vendingmachine.workers

import android.content.Context
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.vendingmachine.R
import com.example.vendingmachine.data.persistence.TaskDatabase

class DailyHabitReset @WorkerInject constructor( @Assisted context: Context, @Assisted params: WorkerParameters, val datasource: TaskDatabase) : Worker(context, params) {

    override fun doWork(): Result {
        val appContext = applicationContext

        return try {
//            val checkedHabits = datasource.getTaskDao().getCompleteHabits()
//
//            checkedHabits.forEach { it ->
//                    it.isCompleted = false
//                    datasource.getTaskDao().updateTask(it)
//            }

            return Result.success()
        } catch (throwable: Throwable) {
            Log.e(appContext.getString(R.string.habit_worker_error_tag), appContext.getString(R.string.habit_worker_error_message), throwable)
            Result.failure()
        }
    }

}