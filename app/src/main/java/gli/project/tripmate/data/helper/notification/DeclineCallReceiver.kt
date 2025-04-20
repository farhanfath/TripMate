package gli.project.tripmate.data.helper.notification

import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

// BroadcastReceiver for declining calls
class DeclineCallReceiver : android.content.BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            NotificationManagerCompat.from(it).cancel(CallNotificationManager.NOTIFICATION_ID)
        }
    }
}