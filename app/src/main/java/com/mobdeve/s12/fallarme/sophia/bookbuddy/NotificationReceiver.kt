package com.mobdeve.s12.fallarme.sophia.bookbuddy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val notificationId = System.currentTimeMillis().toInt()
            val channelId = "default"

            // Create notification channel if needed
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelName = "Default Channel"
                val channelDescription = "Channel for default notifications"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
                    description = channelDescription
                }

                val notificationManager = it.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(it, "default")
                .setSmallIcon(R.drawable.bookbuddy_logo)
                .setContentTitle("Read Reminder")
                .setContentText("It's time to read!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(it)) {
                notify(notificationId, builder.build())
            }
        }
    }
}

