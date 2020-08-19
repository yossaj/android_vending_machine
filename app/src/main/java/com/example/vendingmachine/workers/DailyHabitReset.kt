package com.example.vendingmachine.workers

import android.content.Context
import android.util.Log
import android.util.TimeUtils
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.vendingmachine.R
import com.example.vendingmachine.data.TaskDatabase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

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