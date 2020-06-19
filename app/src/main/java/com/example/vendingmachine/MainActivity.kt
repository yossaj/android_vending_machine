package com.example.vendingmachine

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.vendingmachine.workers.NotificationWorker
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCenter.start(
            application, "2dcf9351-a14e-40bc-9f5d-af0085f16d1c",
            Analytics::class.java, Crashes::class.java
        )

        createChannel(getString(R.string.remaining_tasks_id), "Remaining Tasks", this)
        triggerNotificationWorker()
    }

    fun createChannel(channelId: String, channelName: String, activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Remaining Tasks"
            notificationChannel.setShowBadge(false)

            val notificationManager = activity.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun triggerNotificationWorker() {

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, 15)
        dueDate.set(Calendar.MINUTE, 50)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        val notificationWorkBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()
        val buildNotificationRequest =
            notificationWorkBuilder
                .addTag("Test Notification")
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()
        WorkManager.getInstance(this).enqueue(buildNotificationRequest)
    }
}


