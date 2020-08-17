package com.example.vendingmachine

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.vendingmachine.ui.home.HomeViewModel
import com.example.vendingmachine.workers.DailyHabitReset
import com.example.vendingmachine.workers.NotificationWorker
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel : HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCenter.start(
            application, "2dcf9351-a14e-40bc-9f5d-af0085f16d1c",
            Analytics::class.java, Crashes::class.java
        )
        createChannel(getString(R.string.remaining_tasks_id), "Remaining Tasks", this)
        triggerNotificationWorker()
        uncheckDailyHabits()
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

       val timeDiff = setTimeDiff(15, 33)

        val notificationWorkBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()
        val buildNotificationRequest =
            notificationWorkBuilder
                .addTag("Test Notification")
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(buildNotificationRequest)
    }

    fun uncheckDailyHabits(){

        val resetHabitRequest = OneTimeWorkRequestBuilder<DailyHabitReset>()
        val builtRequest = resetHabitRequest
            .addTag("Reset Habit Request")
            .setInitialDelay(setTimeDiff(16, 16), TimeUnit.MILLISECONDS)
            .build()
        val workManager = WorkManager.getInstance(this)
        workManager.pruneWork()
        workManager.enqueue(builtRequest)
    }

    fun setTimeDiff(hour : Int, minute : Int) : Long{
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()

        dueDate.set(Calendar.HOUR_OF_DAY, hour)
        dueDate.set(Calendar.MINUTE, minute)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        return timeDiff
    }


}


