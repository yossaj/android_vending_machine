package com.example.vendingmachine.ui

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.vendingmachine.R
import com.example.vendingmachine.databinding.ActivityMainBinding
import com.example.vendingmachine.workers.NotificationWorker
import com.google.firebase.firestore.FirebaseFirestore
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

            AppCenter.start(
                application, "2dcf9351-a14e-40bc-9f5d-af0085f16d1c",
                Analytics::class.java, Crashes::class.java
            )
        createChannel(getString(R.string.remaining_tasks_id), "Remaining Tasks", this)
        FirebaseFirestore.setLoggingEnabled(true);
        triggerNotificationWorker()
        setContentView(binding.root)
        setUpBottomNavBar(binding)

    }

    private fun setUpBottomNavBar(binding: ActivityMainBinding) {
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.tasksFragment,
                R.id.habitFragment,
                R.id.settingsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar?.hide()
        binding.navView.setupWithNavController(navController)
        binding.navView.setOnNavigationItemReselectedListener {
            //            do nothing - prevent fragments from loading twice
        }
    }

    fun createChannel(channelId: String, channelName: String, activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
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
       val timeDiff = setTimeDiff(9, 40)

        val notificationWorkBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()
        val buildNotificationRequest =
            notificationWorkBuilder
                .addTag("Test Notification")
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniqueWork("Remaining Tasks",  ExistingWorkPolicy.KEEP, buildNotificationRequest)
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


