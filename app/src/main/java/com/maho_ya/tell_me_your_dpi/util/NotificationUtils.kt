package com.maho_ya.tell_me_your_dpi.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.maho_ya.tell_me_your_dpi.R

object NotificationUtils {

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelForOOrHigher(context)
        }
    }

    // Android 8.0以上では通知チャネルを作成する必要がある
    // https://developer.android.com/training/notify-user/build-notification#Priority
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelForOOrHigher(context: Context) {
        val channelId = context.getString(R.string.default_notification_channel_id)
        val name = context.getString(R.string.default_notification_channel_name)
        val descriptionText = context.getString(R.string.default_notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}