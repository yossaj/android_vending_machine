package com.example.vendingmachine.workers

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.vendingmachine.data.persistence.TaskDatabase
import com.example.vendingmachine.notifcation.sendNotificaiton
import com.example.vendingmachine.utils.Constants
import com.example.vendingmachine.utils.Constants.TASKS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

class NotificationWorker @WorkerInject
constructor(@Assisted val context: Context,
            @Assisted params: WorkerParameters,
            private val remoteDb: FirebaseFirestore,
            private val firebaseAuth: FirebaseAuth
) : Worker(context, params){

    override fun doWork(): Result {

        try {
            val userId = if(firebaseAuth.currentUser != null) firebaseAuth.currentUser!!.uid else  return Result.retry()
            val querySnapshot = remoteDb
                .collection(TASKS)
                .document(userId)
                .collection("tasks")
                .whereEqualTo("completed", false).get().result

            val remainingTasksCount = if(querySnapshot != null) querySnapshot!!.documents.size else 0
            var title = "You have $remainingTasksCount tasks left to do today"
            var message = ""
            if(remainingTasksCount == 0){
                title = "Add a new task"
                message = "Get some shit done today!"
            }

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.sendNotificaiton(title, message ,applicationContext)

            Result.success()
        }catch (exception : IOException){
            Log.e("notification_error", exception.toString())
            return Result.failure()
        }catch (throwable : Throwable){
            Log.e("notification_error", throwable.toString())
            return Result.failure()
        }
        return Result.retry()
    }

}