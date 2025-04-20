package gli.project.tripmate.data.helper.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import gli.project.tripmate.presentation.activity.call.ReceiveCallActivity

class CallNotificationManager(private val context: Context) {
    companion object {
        const val CHANNEL_ID = "incoming_call_channel"
        const val NOTIFICATION_ID = 1001
        const val REQUEST_CODE = 0
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Incoming Calls"
            val descriptionText = "Notifications for incoming customer service calls"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = android.graphics.Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE),
                    null
                )
            }

            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
            Log.d("CallNotificationManager", "Notification channel created")
        }
    }

    fun showIncomingCallNotification(
        appId: String,
        channelName: String,
        token: String,
        title: String = "Incoming Call",
        message: String = "User Need Help"
    ) {
        // Create an intent to open the call activity
        val intent = Intent(context, ReceiveCallActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("EXTRA_APP_ID", appId)
            putExtra("EXTRA_CHANNEL_NAME", channelName)
            putExtra("EXTRA_TOKEN", token)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setOngoing(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .setContentIntent(pendingIntent)
            .addAction(
                android.R.drawable.ic_menu_call,
                "Answer",
                pendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Decline",
                createDeclinePendingIntent()
            )
            .setFullScreenIntent(pendingIntent, true)
            .build()

        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(NOTIFICATION_ID, notification)
                    Log.d("CallNotificationManager", "Notification shown")
                } else {
                    Log.e("CallNotificationManager", "Notification permission not granted")
                }
            } else {
                notify(NOTIFICATION_ID, notification)
                Log.d("CallNotificationManager", "Notification shown (pre-Tiramisu)")
            }
        }
    }

    private fun createDeclinePendingIntent(): PendingIntent {
        val intent = Intent(context, DeclineCallReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE + 1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun cancelNotification() {
        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
    }
}

