package com.example.vendingmachine.notifcation

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.vendingmachine.MainActivity
import com.example.vendingmachine.R

fun NotificationManager.sendNotificaiton(titleText : String, message : String,applicationContext: Context){

    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        1,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.remaining_tasks_id))
        .setContentText(message)
        .setContentTitle(titleText)
        .setSmallIcon(R.drawable.kanye)
        .setAutoCancel(true)
        .setContentIntent(contentPendingIntent)
        .addAction(
            R.drawable.kanye,
            applicationContext.getString(R.string.remaining_tasks_id),
            contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    notify(1, builder.build())

}