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
import com.example.vendingmachine.utils.Constants.USERS
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
            val userId = if(firebaseAuth.currentUser != null) firebaseAuth.currentUser!!.uid else "NULL"

            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            var remainingTasksCount = 0
            var message = ""
            var title = ""

            val docRef = remoteDb
                .collection(USERS)
                .document(userId)
                .collection(TASKS)
                .whereEqualTo("completed", false)

            docRef.get()
                .addOnSuccessListener { response ->

                    if (response != null) {
                        remainingTasksCount = response.size()
                        title = "You have $remainingTasksCount tasks left to do today"
                        message = response.documents[0]["title"].toString()
                        notificationManager.sendNotificaiton(title, message ,applicationContext)
                        Result.success()
                    } else {
                        if(remainingTasksCount == 0){
                            title = "Add a new task"
                            message = "Get some shit done today!"
                            notificationManager.sendNotificaiton(title, message ,applicationContext)
                            Result.success()
                        }
                        Log.d("FIREBASELOG", "No such collection")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("FIREBASELOG", "get failed with ", exception)
                }

        }catch (exception : IOException){
            Log.e("notification_error_exc", exception.toString())
            return Result.failure()
        }catch (throwable : Throwable){
            Log.e("notification_error_thr", throwable.toString())
            return Result.failure()
        }
        return Result.success()
    }

}