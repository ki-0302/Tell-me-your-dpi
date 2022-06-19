package com.maho_ya.tell_me_your_dpi.domain.services

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.ui.MainActivity
import timber.log.Timber

class AppFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: ${remoteMessage.from}")
        Timber.d("Message data payload: ${remoteMessage.data}")

        // 通知の作成＆タップ後にActivity起動
        // https://developer.android.com/training/notify-user/build-notification#click
        remoteMessage.notification?.let { notification ->
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntentForMOrHigher(intent)
            } else {
                getPendingIntentForLessThanM(intent)
            }

            val builder =
                NotificationCompat.Builder(this, this.getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(notification.title)
                    .setContentText(notification.body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true) // タップしたら通知を削除する

            with(NotificationManagerCompat.from(this)) {
                notify(remoteMessage.messageId.hashCode(), builder.build())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPendingIntentForMOrHigher(intent: Intent) = PendingIntent.getActivity(
        this,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    @Suppress("DEPRECATION")
    private fun getPendingIntentForLessThanM(intent: Intent) = PendingIntent.getActivity(
        this,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}
