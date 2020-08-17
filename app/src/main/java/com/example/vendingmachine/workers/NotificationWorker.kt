package com.example.vendingmachine.workers

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.vendingmachine.R
import com.example.vendingmachine.data.TaskDatabase
import com.example.vendingmachine.notifcation.sendNotificaiton
import java.io.IOException

//class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params){
//
//    override fun doWork(): Result {
//        val appContext = applicationContext
//
//        return try {
//            val datasource = TaskDatabase.getInstance(appContext)
//            val remainingTasks = datasource.taskDao.getIncompleteTasksTitles()
//            val remainingTasksCount = remainingTasks.size.toString()
//            var title = "You have $remainingTasksCount tasks left to do today"
//            var message = ""
//            if(remainingTasks.size == 0){
//                title = "Add a new task"
//                message = "Get some shit done today!"
//            }else{
//                remainingTasks[0]
//            }
//
//            val notificationManager = ContextCompat.getSystemService(
//                appContext,
//                NotificationManager::class.java
//            ) as NotificationManager
//
//            notificationManager.sendNotificaiton(title, message ,applicationContext)
//
//            Result.success()
//        }catch (exception : IOException){
//            Log.e("notification_error", exception.toString())
//            return Result.failure()
//        }catch (throwable : Throwable){
//            Log.e("notification_error", throwable.toString())
//            return Result.failure()
//        }
//    }
//
//}